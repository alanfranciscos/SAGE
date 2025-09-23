package com.sage.dto.v1.resident.request;

import java.time.ZonedDateTime;

/**
 * Data Transfer Object for creating a new resident via JSON requests. This
 * record contains fields necessary to create a resident, with image data
 * represented as a Base64 string instead of MultipartFile.
 *
 * @param fullName The full name of the resident.
 * @param cpf The CPF (Cadastro de Pessoas Físicas) of the resident.
 * @param sex The sex of the resident.
 * @param birthDate The birth date of the resident.
 * @param emergencyName The emergency contact name (optional).
 * @param emergencyPhone The emergency contact phone (optional).
 * @param relationship The relationship to the emergency contact (optional).
 * @param residentialUnit The residential unit where the resident lives.
 * @param controlNumber The control number for the resident.
 */
public record UpdateResidentRequestDto(
        String fullName,
        String cpf,
        char sex,
        ZonedDateTime birthDate,
        String emergencyName,
        String emergencyPhone,
        String relationship,
        String residentialUnit,
        Integer controlNumber
        ) {

    private boolean emergencyValidator() {
        boolean hasAllFields = emergencyName != null && !emergencyName.isEmpty()
                && emergencyPhone != null && !emergencyPhone.isEmpty()
                && relationship != null && !relationship.isEmpty();
        boolean hasAnyFields = (emergencyName != null && !emergencyName.isEmpty())
                || (emergencyPhone != null && !emergencyPhone.isEmpty())
                || (relationship != null && !relationship.isEmpty());
        return hasAnyFields && !hasAllFields;
    }

    public void validate() {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be null or empty");
        }
        if (sex != 'M' && sex != 'F') {
            throw new IllegalArgumentException("Sex must be 'M' or 'F'");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (residentialUnit == null || residentialUnit.isEmpty()) {
            throw new IllegalArgumentException("Residential unit cannot be null or empty");
        }
        if (controlNumber == null) {
            throw new IllegalArgumentException("Control number cannot be null");
        }
        if (this.emergencyValidator()) {
            throw new IllegalArgumentException("Invalid emergency contact information");
        }
    }

}
