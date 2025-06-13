package com.sage.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sage.config.settings.Settings;

@Configuration
public class Database {

    private final Settings settings;

    public Database(
            Settings settings
    ) throws SQLException, IOException {
        this.settings = settings;
    }

    @Bean
    public Connection connectDatabase() throws SQLException, IOException {
        try {
            if (settings.getDbUrl() == null || settings.getDbUser() == null || settings.getDbPassword() == null) {
                throw new IllegalArgumentException("Database connection parameters are not set.");
            }
            return DriverManager.getConnection(
                    settings.getDbUrl(),
                    settings.getDbUser(),
                    settings.getDbPassword()
            );
        } catch (SQLTimeoutException e) {
            throw new IOException(
                    "Database connection timed out: " + e.getMessage(), e
            );
        } catch (SQLException e) {
            throw new SQLException(
                    "Failed to connect to the database: " + e.getMessage(), e
            );
        }
    }
}
