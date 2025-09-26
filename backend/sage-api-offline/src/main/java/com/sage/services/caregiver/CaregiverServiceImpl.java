package com.sage.services.caregiver;

import com.sage.dao.caregiver.CaregiverDaoImpl;
import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.exception.AlreadyExistsException;
import com.sage.exception.NotFoundException;
import com.sage.port.services.caregiver.CaregiverService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
public class CaregiverServiceImpl implements CaregiverService {

    private final CaregiverDaoImpl caregiverDao;

    public CaregiverServiceImpl(CaregiverDaoImpl caregiverDao) {
        this.caregiverDao = caregiverDao;
    }

    @Override
    public UUID createCaregiver(String fullName, String cpf, String email, String phone, String position) {

        caregiverDao.findByCpf(cpf).ifPresent(existingId -> {
            throw new AlreadyExistsException("A caregiver with the same CPF already exists.");
        });

        caregiverDao.findByEmail(email).ifPresent(existingId -> {
            throw new AlreadyExistsException("A caregiver with the same email already exists.");
        });

        caregiverDao.findByPhone(phone).ifPresent(existingId -> {
            throw new AlreadyExistsException("A caregiver with the same phone already exists.");
        });

        UUID organizationId = caregiverDao.getFirstOrganizationId();

        String token = generateUniqueToken();
        return caregiverDao.createCaregiver(fullName, cpf, email, phone, token, organizationId, position);
    }

    @Override
    public List<CaregiverResponseDto> getAllCaregivers(int limit, int skip, String search) {
        return caregiverDao.getAllCaregivers(limit, skip, search);
    }

    @Override
    public void updateCaregiver(UUID id, CreateCaregiverRequestDto request) {
        caregiverDao.findByCpf(request.cpf()).ifPresent(existingId -> {
            if (!existingId.equals(id)) {
                throw new AlreadyExistsException("The CPF is already in use by another caregiver.");
            }
        });

        caregiverDao.findByEmail(request.email()).ifPresent(existingId -> {
            if (!existingId.equals(id)) {
                throw new AlreadyExistsException("The email is already in use by another caregiver.");
            }
        });

        caregiverDao.findByPhone(request.phone()).ifPresent(existingId -> {
            if (!existingId.equals(id)) {
                throw new AlreadyExistsException("The phone is already in use by another caregiver.");
            }
        });

        caregiverDao.updateCaregiver(id, request);
    }

    @Override
    public void updateCaregiverActiveStatus(UUID id, boolean active) {
        caregiverDao.updateCaregiverActiveStatus(id, active);
    }

    @Override
    public CaregiverResponseDto findByToken(String token) {
        return caregiverDao.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Caregiver not found with token: " + token));
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = generateRandomToken();
        } while (caregiverDao.isTokenInUse(token));
        return token;
    }

    private String generateRandomToken() {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String DATA_FOR_RANDOM_STRING = CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }        return sb.toString();
    }
}