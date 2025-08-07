package com.sage.port.services.resident;

import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;

public interface ResidentEmergencyContactService {

    /**
     * Creates a new resident emergency contact based on the provided request
     * DTO.
     *
     * @param requestDto The DTO containing the details of the emergency contact
     * to be created.
     * @return The UUID of the newly created emergency contact.
     */
    UUID create(
            CreateResidentRequestDto requestDto, UUID residentId
    );
}
