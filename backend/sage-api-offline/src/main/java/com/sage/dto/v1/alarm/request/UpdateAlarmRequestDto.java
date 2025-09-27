package com.sage.dto.v1.alarm.request;

public record UpdateAlarmRequestDto(
    String model,
    String status,
    String ipAddress,
    String macAddress,
    String account,
    String serialNumber,
    int port
) {}
