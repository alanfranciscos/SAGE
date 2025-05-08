package com.sage.dto.assist;

import java.time.ZonedDateTime;

public record CreateAssistRequest(ZonedDateTime calledAt) {

    public CreateAssistRequest {
        if (calledAt == null) {
            throw new IllegalArgumentException("calledAt cannot be null");
        }
    }
}
