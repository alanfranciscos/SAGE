package com.sage.dto.health;

/**
 * HealthResponse is a data transfer object that represents the health status of
 * the application. It contains information about the application's status,
 * uptime, version, and timestamp.
 *
 * @param status The health status of the application (e.g., "UP", "DOWN").
 * @param uptime The uptime of the application in a human-readable format.
 * @param version The version of the application.
 * @param timestamp The timestamp when the health check was performed.
 */
public record HealthResponse(
        String status,
        String uptime,
        String version,
        String timestamp
        ) {

}
