package com.sage.port.dao.resident;

import java.util.UUID;

import com.sage.model.resident.Resident;

/**
 * ResidentDao provides methods to manage and retrieve resident information.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public interface ResidentDao {

    /**
     * Creates a new resident with the provided details.
     *
     * @param resident The resident object containing the details to create.
     * @return The UUID of the newly created resident.
     */
    UUID createResident(
            Resident resident
    );

    /**
     * Updates an existing resident with the provided details.
     *
     * @param resident The resident object containing the updated details.
     * @return The updated resident object.
     */
    Resident updateResident(
            Resident resident
    );

    /**
     * finds a resident by their UUID.
     *
     * @param residentId The UUID of the resident to search for.
     * @return The resident object if found, otherwise null.
     */
    Resident findResidentById(
            UUID residentId
    );

    /**
     * Get the total count of residents in the system.
     */
    Long countResidents();
}
