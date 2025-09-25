package com.sage.port.dao.caregiver;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;

import java.util.List;
import java.util.UUID;

public interface CaregiverDao {
    UUID createCaregiver(String fullName, String cpf, String email, String phone, String token, UUID organizationId, String position);
    UUID getFirstOrganizationId();
    boolean isTokenInUse(String token);
    List<CaregiverResponseDto> getAllCaregivers(int limit, int skip, String search);
    void updateCaregiver(UUID id, CreateCaregiverRequestDto request);
}
