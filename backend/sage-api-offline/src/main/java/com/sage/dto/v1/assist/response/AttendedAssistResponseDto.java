package com.sage.dto.v1.assist.response;

import java.util.UUID;

import com.sage.model.assist.SeverityLevel;

public record AttendedAssistResponseDto(
        UUID assistId,
        String patientName,
        String patientUnit,
        String elapsedTime,
        String description,
        SeverityLevel severityLevel
        ) {

}
