package com.sage.dto.v1.assist.response;

import com.sage.model.assist.SeverityLevel;

import java.time.ZonedDateTime;
import java.util.UUID;

public record PendingAssistDetailResponseDto(
        UUID assistId,
        String fullName,
        Integer age,
        String residentialUnit,
        ZonedDateTime elapsedTime,
        SeverityLevel severityLevel,
        String status
) {

}
