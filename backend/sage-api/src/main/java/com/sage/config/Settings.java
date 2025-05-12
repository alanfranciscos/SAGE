package com.sage.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Settings {

    final private static Dotenv dotenv = Dotenv.load();

    final private static String VERSION = dotenv.get("VERSION");

    final private static String DB_URL = dotenv.get("DB_URL");
    final private static String DB_USER = dotenv.get("SQL_USERNAME");
    final private static String DB_PASSWORD = dotenv.get("SQL_PASSWORD");

    public static String getVersion() {
        return VERSION;
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}
