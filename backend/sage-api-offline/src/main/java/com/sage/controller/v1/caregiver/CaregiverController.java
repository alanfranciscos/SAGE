package com.sage.controller.v1.caregiver;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.services.caregiver.CaregiverServiceImpl;

/**
 * CaregiverController provides endpoints for managing caregivers in the system.
 * It allows for creating caregivers with unique tokens and associating them with organizations.
 *
 * @author SAGE System
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/caregiver")
public class CaregiverController {

    private final CaregiverServiceImpl caregiverService;

    public CaregiverController(
            CaregiverServiceImpl caregiverService
    ) {
        this.caregiverService = caregiverService;
    }

    /**
     * Creates a new caregiver with a unique 8-character alphanumeric token.
     * The caregiver is automatically associated with the first organization in the database.
     *
     * @param createCaregiverRequestDto The request body containing caregiver data
     * @return ResponseEntity with the created caregiver information and location header
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCaregiver(
            @RequestBody CreateCaregiverRequestDto createCaregiverRequestDto
    ) {
        createCaregiverRequestDto.validate();
        
        Map<String, Object> createdCaregiver = this.caregiverService.createCaregiver(createCaregiverRequestDto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCaregiver.get("id"))
                .toUri();
        
        return ResponseEntity.created(location).body(createdCaregiver);
    }
}
