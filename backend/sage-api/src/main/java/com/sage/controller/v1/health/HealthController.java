package com.sage.controller.v1.health;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sage.config.settings.Settings;
import com.sage.dto.health.HealthResponse;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    private static final Instant START_TIME = Instant.now();
    private final String VERSION;

    public HealthController(Settings settings) {
        this.VERSION = settings.getVersion();
    }

    @GetMapping
    public HealthResponse getHealth() {
        Instant now = Instant.now();
        Duration uptime = Duration.between(START_TIME, now);

        return new HealthResponse(
                "ok",
                uptime.getSeconds() + "s",
                VERSION,
                DateTimeFormatter.ISO_INSTANT.format(now)
        );
    }
}
