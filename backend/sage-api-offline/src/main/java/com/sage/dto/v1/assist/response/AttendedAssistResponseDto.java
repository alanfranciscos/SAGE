package com.sage.dto.v1.assist.response;

import com.sage.model.assist.SeverityLevel;

import java.time.ZonedDateTime;
import java.util.UUID;

public record AttendedAssistResponseDto(
    UUID assistId,
    String patientName,
    String patientUnit,
    ZonedDateTime elapsedTime,
    String description,
    SeverityLevel severityLevel
) {}
