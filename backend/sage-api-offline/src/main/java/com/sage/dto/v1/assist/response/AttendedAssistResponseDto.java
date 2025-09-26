package com.sage.dto.v1.assist.response;

import com.sage.model.assist.SeverityLevel;
import java.util.UUID;

public record AttendedAssistResponseDto(
    UUID assistId,
    String patientName,
    String patientUnit,
    String elapsedTime,
    String description,
    SeverityLevel severityLevel
) {}
