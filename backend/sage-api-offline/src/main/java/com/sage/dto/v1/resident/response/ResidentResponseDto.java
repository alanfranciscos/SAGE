package com.sage.dto.v1.resident.response;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for representing a resident's response. This record
 * contains fields necessary to provide a detailed view of a resident, including
 * their personal information, residential unit, and status.
 *
 * @param id The unique identifier of the resident.
 * @param fullName The full name of the resident.
 * @param cpf The CPF (Cadastro de Pessoas Físicas) of the resident.
 * @param sex The sex of the resident.
 * @param birthDate The birth date of the resident.
 * @param createdAt The date and time when the resident was created.
 * @param updatedAt The date and time when the resident was last updated.
 * @param residentialUnit The residential unit where the resident lives.
 * @param imageData The image data of the resident, typically in base64 format.
 * @param active The status of the resident (active/inactive).
 */
public record ResidentResponseDto(
        UUID id,
        String fullName,
        String cpf,
        char sex,
        ZonedDateTime birthDate,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        String residentialUnit,
        String imageData,
        boolean active
        ) {

}
