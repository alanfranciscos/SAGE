package com.sage.dto.v1.assist.response;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.sage.model.assist.SeverityLevel;

public record PendingAssistResponseDto(
        UUID assistId,
        String fullName,
        String residentialUnit,
        ZonedDateTime elapsedTime,
        SeverityLevel severityLevel,
        String status
        ) {

}
