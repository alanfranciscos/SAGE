package com.sage.dto.v1.control.response;

import java.util.UUID;

public record ControlResponseDto(
        UUID id,
        Integer controlId,
        UUID residentId
        ) {

    public ControlResponseDto(UUID id, Integer controlId, UUID residentId) {
        this.id = id;
        this.controlId = controlId;
        this.residentId = residentId;
    }

}
