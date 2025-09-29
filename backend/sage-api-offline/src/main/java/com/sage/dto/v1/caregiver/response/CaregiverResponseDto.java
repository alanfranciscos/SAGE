package com.sage.dto.v1.caregiver.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CaregiverResponseDto(
        UUID id,
        String fullName,
        String phone,
        String email,
        String cpf,
        String email,
        String phone,
        String token,
        boolean active,
        OffsetDateTime lastUsedToken
        ) {

}
