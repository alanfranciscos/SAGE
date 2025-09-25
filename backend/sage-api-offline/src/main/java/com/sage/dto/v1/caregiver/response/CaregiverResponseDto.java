package com.sage.dto.v1.caregiver.response;

import java.time.OffsetDateTime;

public record CaregiverResponseDto(
        String fullName,
        String cpf,
        String token,
        boolean active,
        OffsetDateTime lastUsedToken
) {
}
