package com.sage.config.settings;

public class Settings {

    private final String version;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public Settings(String version, String dbUrl, String dbUser, String dbPassword) {
        this.version = version;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getVersion() {
        return version;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
