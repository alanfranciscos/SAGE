package com.sage.services.caregiver;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sage.dao.caregiver.CaregiverDaoImpl;

@Service
public class CaregiverServiceImpl {

    private final CaregiverDaoImpl caregiverDao;

    public CaregiverServiceImpl(CaregiverDaoImpl caregiverDao) {
        this.caregiverDao = caregiverDao;
    }

    public UUID createCaregiver(String fullName, String cpf, String email, String phone, String position) {
        String token = generateUniqueToken();
        UUID organizationId = caregiverDao.getFirstOrganizationId();
        return caregiverDao.createCaregiver(fullName, cpf, email, phone, token, organizationId, position);
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
        }
        return sb.toString();
    }
}
