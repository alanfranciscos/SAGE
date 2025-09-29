package com.sage.dto.v1.caregiver.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CaregiverResponseFromPasswordTableDto(
        UUID id,
        UUID caregiver_id,
        String caregiver_password,
        OffsetDateTime created_at,
        boolean active,
        boolean staging,
        OffsetDateTime  verification_code,
        OffsetDateTime  code_valid_until

) {
}