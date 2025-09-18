package com.sage.port.dao.resident;

import java.util.UUID;

import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.model.resident.emergency.ResidentEmergencyContact;

public interface ResidentEmergencyContactDao {

    /**
     * Creates a new resident emergency contact with the provided details.
     *
     * @param emergencyContact The ResidentEmergencyContact object containing
     * the details to create.
     * @return The UUID of the newly created resident emergency contact.
     */
    public UUID create(ResidentEmergencyContact emergencyContact);

    /**
     * Retrieves a resident emergency contact by its client ID.
     *
     * @param clientId The UUID of the client to retrieve.
     * @return The ResidentEmergencyContact object associated with the given
     * client ID.
     */
    public ResidentEmergencyContact getByClientId(UUID clientId);

    /**
     * Updates an existing resident emergency contact with the provided details.
     *
     * @param requestDto The UpdateResidentRequestDto object containing the
     * updated details.
     */
    public void update(UpdateResidentRequestDto requestDto, UUID residentId);

}
