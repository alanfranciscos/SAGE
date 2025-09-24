package com.sage.services.caregiver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.dao.caregiver.CaregiverDaoImpl;
import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.model.caregiver.Caregiver;
import com.sage.util.TokenGenerator;

@Service
public class CaregiverServiceImpl {

    private static final Logger logger = Logger.getLogger(CaregiverServiceImpl.class.getName());

    private final CaregiverDaoImpl caregiverDao;

    public CaregiverServiceImpl(CaregiverDaoImpl caregiverDao) {
        this.caregiverDao = caregiverDao;
    }

    /**
     * Creates a new caregiver with a unique token and associates it with the first organization.
     *
     * @param requestDto The request containing caregiver data
     * @return A map containing the created caregiver information
     */
    public Map<String, Object> createCaregiver(CreateCaregiverRequestDto requestDto) {
        logger.info("Creating new caregiver: " + requestDto.fullName());
        
        // Get the first organization ID
        UUID organizationId = caregiverDao.getFirstOrganizationId();
        
        // Generate unique token
        String token = TokenGenerator.generateUniqueToken();
        logger.info("Generated token for caregiver: " + token);
        
        // Create caregiver object
        Caregiver caregiver = Caregiver.mapFromCreateCaregiverRequestDto(requestDto, organizationId, token);
        
        // Save to database
        UUID caregiverId = caregiverDao.createCaregiver(caregiver);
        caregiver.setId(caregiverId);
        
        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("id", caregiverId);
        response.put("organizationId", organizationId);
        response.put("fullName", caregiver.getFullName());
        response.put("phone", caregiver.getPhone());
        response.put("email", caregiver.getEmail());
        response.put("cpf", caregiver.getCpf());
        response.put("token", caregiver.getToken());
        response.put("active", caregiver.isActive());
        
        logger.info("Successfully created caregiver with ID: " + caregiverId);
        return response;
    }
}
