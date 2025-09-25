package com.sage.port.dao.caregiver;

import java.util.UUID;

public interface CaregiverDao {
    UUID createCaregiver(String fullName, String cpf, String email, String phone, String token, UUID organizationId, String position);
    UUID getFirstOrganizationId();
    boolean isTokenInUse(String token);
}
