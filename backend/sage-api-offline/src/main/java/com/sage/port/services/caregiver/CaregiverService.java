package com.sage.port.services.caregiver;

import java.util.UUID;

public interface CaregiverService {
    UUID createCaregiver(String fullName, String cpf, String email, String phone, String position);
}
