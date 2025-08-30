package com.sage.dao.sse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sage.config.settings.Settings;

public class BaseSSE {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    protected final Connection conn;

    public BaseSSE(Settings settings) {
        this.dbUrl = settings.getDbUrl();
        this.dbUser = settings.getDbUser();
        this.dbPassword = settings.getDbPassword();

        try {
            this.conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database to SSE listener", e);
        }
    }

    protected Connection getConnection() {
        return conn;
    }
}
