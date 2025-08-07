package com.sage.port.dao.resident;

import java.util.UUID;

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

}
