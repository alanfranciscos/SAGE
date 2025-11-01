package com.sage.port.services.caregiver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sage.dto.v1.auth.ResetPasswordRequestDTO;
import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseFromPasswordTableDto;

public interface CaregiverService {

    UUID createCaregiver(String fullName, String cpf, String email, String phone, String position);

    List<CaregiverResponseDto> getAllCaregivers(int limit, int skip, String search);

    void updateCaregiver(UUID id, CreateCaregiverRequestDto request);

    void updateCaregiverActiveStatus(UUID id, boolean active);

    CaregiverResponseDto findByToken(String token);

    Optional<CaregiverResponseDto> findByEmailAndReturnsCaregiverResponseDto(String email);

    Optional<CaregiverResponseFromPasswordTableDto> getCaregiverFromPasswordTable(UUID uuid);

    CaregiverResponseDto getCaregiverById(UUID id);

    UUID createPassword(UUID caregiverId, String rawPassword);

    Long getCountCaregiverLeader();

    void sendRecoveryToken(String email);

    void resetPassword(ResetPasswordRequestDTO dto, String passwordHash);

    List<CaregiverResponseDto> getAllCaregivers();

}
