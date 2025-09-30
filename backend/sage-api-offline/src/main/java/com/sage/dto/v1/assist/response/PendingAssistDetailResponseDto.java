package com.sage.dto.v1.assist.response;

import java.util.UUID;

import com.sage.model.assist.SeverityLevel;

public record PendingAssistDetailResponseDto(
        UUID assistId,
        String fullName,
        Integer age,
        String residentialUnit,
        String elapsedTime,
        SeverityLevel severityLevel,
        String status
        ) {

}
