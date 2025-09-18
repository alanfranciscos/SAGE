package com.sage.dto.v1.assist.request;

import java.time.ZonedDateTime;

public record CreateAssistRequestDto(
        Integer controlId,
        ZonedDateTime calledAt
        ) {

    public CreateAssistRequestDto  {
        if (controlId == null) {
            throw new IllegalArgumentException("controlId cannot be null");
        }
        if (calledAt == null) {
            throw new IllegalArgumentException("calledAt cannot be null");
        }
    }
}
