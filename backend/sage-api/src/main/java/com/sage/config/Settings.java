package com.sage.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Settings {

    final private static Dotenv dotenv = Dotenv.load();

    public static String getVersion() {
        return dotenv.get("VERSION");
    }
}
