package com.sage.config.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class SettingsConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String sqlUsername;

    @Value("${spring.datasource.password}")
    private String sqlPassword;

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }

    @Bean
    public Settings settings(Dotenv dotenv) {
        String version = dotenv.get("VERSION");
        if (version == null || version.isEmpty()) {
            version = "Local";
        }
        return new Settings(
                version,
                dbUrl,
                sqlUsername,
                sqlPassword
        );
    }
}
