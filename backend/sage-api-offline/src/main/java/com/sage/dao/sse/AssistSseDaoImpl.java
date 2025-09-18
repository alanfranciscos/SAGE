package com.sage.dao.sse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sage.config.settings.Settings;
import com.sage.model.sse.AssistEvent;
import com.sage.port.dao.sse.AssistSseDao;

import jakarta.annotation.PostConstruct;

public class AssistSseDaoImpl extends BaseSSE implements AssistSseDao {

    private final Connection connection;

    public AssistSseDaoImpl(Settings settings) {
        super(settings);
        this.connection = getConnection();
    }

    private static final Logger logger = Logger.getLogger(AssistSseDaoImpl.class.getName());

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        new Thread(this::listenToPostgres, "PG-Notify-Listener").start();
    }

    private void retryingConnection() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void listenToPostgres() {
        while (true) {
            try (this.connection) {
                PGConnection pgConn = this.connection.unwrap(PGConnection.class);

                try (Statement stmt = this.connection.createStatement()) {
                    stmt.execute("LISTEN caregiver_assignment_channel");
                    logger.log(Level.INFO, "[SSE] Listening on channel: caregiver_assignment_channel");
                }

                while (!Thread.currentThread().isInterrupted()) {
                    // Block until a notification is received
                    // or timeout after 10 seconds
                    PGNotification[] notifications = pgConn.getNotifications(5000);
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            handleNotification(notification.getParameter());
                        }
                    } else {
                        // Keep-alive optional
                        try (Statement stmt = this.connection.createStatement()) {
                            stmt.execute("SELECT 1");
                        }
                    }
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "[SSE] Error in listener: {0}", e.getMessage());
                retryingConnection();
            }
        }
    }

    private void handleNotification(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            AssistEvent event = AssistEvent.parseJson(jsonObject);
            logger.log(Level.INFO, "[SSE] Received notification: {0}", event);

            broadcast(event);
        } catch (JSONException e) {
            logger.log(Level.SEVERE, "[SSE] Failed to parse or send notification: {0}", e.getMessage());
        }
    }

    private void broadcast(AssistEvent event) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("assignment-change")
                        .data(event));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        }
    }

    @Override
    public SseEmitter addEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        // Initial message to the client to confirm connection
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE connection established"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(emitter);
        }

        return emitter;
    }

}
