package com.sage.dto.v1.assist.response;

import java.util.List;

public record PaginatedAttendedAssistResponseDto(
    List<AttendedAssistResponseDto> data,
    long total,
    int limit,
    int skip
) {}
