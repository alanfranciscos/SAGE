package com.sage.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Settings {

    final private static Dotenv dotenv = Dotenv.load();

    final private static String VERSION = dotenv.get("VERSION");

    public static String getVersion() {
        return VERSION;
    }
}
