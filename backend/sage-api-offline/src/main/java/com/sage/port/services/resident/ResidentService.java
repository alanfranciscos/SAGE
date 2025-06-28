package com.sage.port.services.resident;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.google.api.client.util.DateTime;
import com.sage.model.resident.Resident;
import com.sage.model.resident.ResidentHeader;
import com.sage.model.resident.ResidentList;

/**
 * ResidentService provides methods to manage residents in the system. It allows
 * for creating, updating, deleting, and retrieving resident information.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public interface ResidentService {

    /**
     * Creates a new resident with the provided details.
     *
     * @param fullName The full name of the resident.
     * @param cpf The CPF (Cadastro de Pessoas Físicas) of the resident.
     * @param sex The sex of the resident.
     * @param birthDate The birth date of the resident.
     * @param createdAt The creation date of the resident record.
     * @param updatedAt The last update date of the resident record.
     * @param residentialUnit The residential unit of the resident.
     * @param imageData The image data of the resident.
     * @return The UUID of the created resident.
     */
    UUID createResident(
            String fullName,
            String cpf,
            char sex,
            DateTime birthDate,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            String residentialUnit,
            String imageData
    );

    /**
     * Updates an existing resident with the provided details.
     *
     * @param id The UUID of the resident to update.
     * @param fullName The new full name of the resident.
     * @param cpf The new CPF of the resident.
     * @param sex The new sex of the resident.
     * @param birthDate The new birth date of the resident.
     * @param updatedAt The new last update date of the resident record.
     * @param residentialUnit The new residential unit of the resident.
     * @param imageData The new image data of the resident.
     * @return The updated resident.
     */
    Resident updateResident(
            UUID id,
            String fullName,
            char sex,
            DateTime birthDate,
            ZonedDateTime updatedAt,
            String residentialUnit,
            String imageData
    );

    /**
     * Lists all residents in the system, categorized by their status.
     *
     * @return List of resident headers.
     */
    ResidentList listResidents();

    /**
     * Deletes a resident by their UUID.
     *
     * @param id The UUID of the resident to delete.
     * @return true if the resident exist
     */
    Resident getResidentDetailsById(
            UUID id
    );

    /**
     * Searches for a resident by their ID.
     *
     * @param id The UUID of the resident to search for.
     * @return The resident if found, null otherwise.
     */
    List<ResidentHeader> searchResidentById(
            UUID id
    );

}
