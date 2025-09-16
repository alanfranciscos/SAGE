package com.sage.port.services.resident;

import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.model.resident.emergency.ResidentEmergencyContact;

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

    /**
     * Retrieves a resident emergency contact by its client ID.
     *
     * @param clientId The UUID of the client to retrieve.
     * @return The ResidentEmergencyContact object associated with the given
     * client ID.
     */
    ResidentEmergencyContact getByClientId(UUID clientId);


    /* Updates an existing resident emergency contact. */
    void update(UpdateResidentRequestDto requestDto, UUID residentId);
}
