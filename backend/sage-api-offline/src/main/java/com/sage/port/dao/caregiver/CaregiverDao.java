package com.sage.port.dao.caregiver;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseFromPasswordTableDto;

public interface CaregiverDao {
    UUID createCaregiver(String fullName, String cpf, String email, String phone, String token, UUID organizationId, String position);
    UUID getFirstOrganizationId();
    boolean isTokenInUse(String token);
    List<CaregiverResponseDto> getAllCaregivers(int limit, int skip, String search);
    void updateCaregiver(UUID id, CreateCaregiverRequestDto request);

    void updateCaregiverActiveStatus(UUID id, boolean active);

    Optional<UUID> findByCpf(String cpf);

    Optional<UUID> findByEmail(String email);

    Optional<UUID> findByPhone(String phone);

    Optional<CaregiverResponseDto> findByToken(String token);

    Optional<CaregiverResponseDto> findByEmailAndReturnsCaregiverResponseDto(String email);

    Optional<CaregiverResponseFromPasswordTableDto> getCaregiverFromPasswordTable(UUID uuid);
    
    Optional<CaregiverResponseDto> findById(UUID id);

    UUID createPassword(UUID caregiverId, String hashedPassword, String verificationCode, OffsetDateTime codeValidUntil);

    Long getCountCaregiverLeader();
}