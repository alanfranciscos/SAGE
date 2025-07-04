package com.sage.dto.v1.resident.request;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for updating a resident's information. This record
 * contains fields necessary to update a resident's details, including their
 * personal information, residential unit, and image data.
 *
 * @param id The unique identifier of the resident to be updated.
 * @param fullName The full name of the resident.
 * @param sex The sex of the resident.
 * @param birthDate The birth date of the resident.
 * @param updatedAt The date and time when the resident's information was last
 * updated.
 * @param residentialUnit The residential unit where the resident lives.
 * @param imageData The image data of the resident, typically in base64 format.
 */
public record UpdateResidentRequestDto(UUID id,
        String fullName,
        char sex,
        ZonedDateTime birthDate,
        ZonedDateTime updatedAt,
        String residentialUnit,
        String imageData
        ) {

}
