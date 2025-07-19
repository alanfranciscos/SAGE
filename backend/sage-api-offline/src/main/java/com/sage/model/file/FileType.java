package com.sage.model.file;

public enum FileType {
    RESIDENT_IMAGE("resident-image"),
    CARREGIVER_IMAGE("caregiver-image");

    private final String type;

    FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
