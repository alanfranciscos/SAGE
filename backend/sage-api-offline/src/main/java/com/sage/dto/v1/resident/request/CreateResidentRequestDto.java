package com.sage.dto.v1.resident.request;

import java.time.ZonedDateTime;

/**
 * Data Transfer Object for creating a new resident. This record contains fields
 * necessary to create a resident, including their personal information,
 * residential unit, and image data.
 *
 * @param fullName The full name of the resident.
 * @param cpf The CPF (Cadastro de Pessoas Físicas) of the resident.
 * @param sex The sex of the resident.
 * @param birthDate The birth date of the resident.
 * @param createdAt The date and time when the resident was created.
 * @param updatedAt The date and time when the resident was last updated.
 * @param residentialUnit The residential unit where the resident lives.
 * @param imageData The image data of the resident, typically in base64 format.
 */
public record CreateResidentRequestDto(
        String fullName,
        String cpf,
        char sex,
        com.google.api.client.util.DateTime birthDate,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        String residentialUnit,
        String imageData
        ) {

}
