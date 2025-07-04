package com.sage.model.assist;

/**
 * Enum representing the severity levels for assistance requests. This enum is
 * used to categorize the urgency of assistance needed.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public enum SeverityLevel {
    WARNING("warning"),
    EMERGENCY("emergency");

    private final String value;

    /**
     * Constructor for SeverityLevel enum.
     *
     * @param value The string representation of the severity level.
     */
    SeverityLevel(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the severity level.
     *
     * @return The string value of the severity level.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the SeverityLevel enum corresponding to the given string value.
     *
     * @param value The string representation of the severity level.
     * @return The SeverityLevel enum corresponding to the value.
     * @throws IllegalArgumentException if the value does not match any severity
     * level.
     */
    public static SeverityLevel fromValue(String value) {
        for (SeverityLevel level : SeverityLevel.values()) {
            if (level.value.equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown severity level: " + value);
    }
}
