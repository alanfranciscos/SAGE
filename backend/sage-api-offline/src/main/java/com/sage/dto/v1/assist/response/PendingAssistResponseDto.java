package com.sage.dto.v1.assist.response;

import java.util.UUID;

import com.sage.model.assist.SeverityLevel;

public record PendingAssistResponseDto(
        UUID assistId,
        String fullName,
        String residentialUnit,
        String elapsedTime,
        SeverityLevel severityLevel,
        String status,
        String imageData
) {

}
