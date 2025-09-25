package com.sage.dto.v1.caregiver.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCaregiverActiveStatusRequestDto(
        @JsonProperty("active")
        boolean active
) {
}
