package com.sage.dto.v1.assist.response;

import java.util.List;

public record PaginatedPendingAssistResponseDto(
        List<PendingAssistResponseDto> data,
        long total,
        int limit,
        int skip
) {
}
