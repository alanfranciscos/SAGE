package com.sage.port.services.caregiver;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseFromPasswordTableDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CaregiverService {
    UUID createCaregiver(String fullName, String cpf, String email, String phone, String position);
    List<CaregiverResponseDto> getAllCaregivers(int limit, int skip, String search);
    void updateCaregiver(UUID id, CreateCaregiverRequestDto request);

    void updateCaregiverActiveStatus(UUID id, boolean active);

    CaregiverResponseDto findByToken(String token);

    Optional<CaregiverResponseDto> findByEmailAndReturnsCaregiverResponseDto(String email);

    Optional<CaregiverResponseFromPasswordTableDto> getCaregiverFromPasswordTable(UUID uuid);
}
