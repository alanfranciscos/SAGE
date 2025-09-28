package com.sage.dto.v1.alarm.response;

import java.util.UUID;

public record AlarmResponseDto(
    UUID id,
    String model,
    String status,
    String ipAddress,
    String macAddress,
    String account,
    String serialNumber,
    int port
) {}
