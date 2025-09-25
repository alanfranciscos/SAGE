package com.sage.port.services.caregiver;

import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;

import java.util.List;
import java.util.UUID;

public interface CaregiverService {
    UUID createCaregiver(String fullName, String cpf, String email, String phone, String position);
    List<CaregiverResponseDto> getAllCaregivers();
}
