package com.sage.dto.v1.resident.response;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for resident details response. This record contains
 * fields necessary to represent a resident's details, including personal
 * information and emergency contact details.
 *
 * @param id The unique identifier of the resident.
 * @param fullName The full name of the resident.
 * @param cpf The CPF (Cadastro de Pessoas Físicas) of the resident.
 * @param sex The sex of the resident.
 * @param birthDate The birth date of the resident.
 * @param createdAt The date and time when the resident was created.
 * @param updatedAt The date and time when the resident was last updated.
 * @param active The status of the resident (active/inactive).
 * @param imageData The image data of the resident, typically in byte array
 * format.
 * @param emergencyName The name of the emergency contact.
 * @param emergencyPhone The phone number of the emergency contact.
 * @param relationship The relationship of the emergency contact to the
 * resident.
 * @param residentialUnit The residential unit where the resident lives.
 * @param controlNumber The control number of the residential unit.
 */
public record ResidentDetailResponseDto(
        // Resident personal information
        UUID id,
        String fullName,
        String cpf,
        char sex,
        ZonedDateTime birthDate,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        boolean active,
        byte[] imageData,
        // Emergency contact details
        String emergencyName,
        String emergencyPhone,
        String relationship,
        // Residential unit details
        String residentialUnit,
        Integer controlNumber
        ) {

    public ResidentDetailResponseDto toResidentDetailResponseDto() {
        return new ResidentDetailResponseDto(
                id,
                fullName,
                cpf,
                sex,
                birthDate,
                createdAt,
                updatedAt,
                active,
                imageData,
                emergencyName,
                emergencyPhone,
                relationship,
                residentialUnit,
                controlNumber
        );
    }

}
