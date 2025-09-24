package com.sage.dto.v1.caregiver.request;

/**
 * Data Transfer Object for creating a new caregiver via JSON requests.
 * This record contains fields necessary to create a caregiver.
 *
 * @param fullName The full name of the caregiver.
 * @param phone The phone number of the caregiver (must be unique).
 * @param email The email address of the caregiver (must be unique).
 * @param cpf The CPF (Cadastro de Pessoas Físicas) of the caregiver (must be unique).
 */
public record CreateCaregiverRequestDto(
        String fullName,
        String phone,
        String email,
        String cpf
) {

    public void validate() {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF cannot be null or empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must be a valid email address");
        }
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF must have 11 digits");
        }
        // Basic phone validation - at least 10 digits
        String phoneDigits = phone.replaceAll("[^0-9]", "");
        if (phoneDigits.length() < 10) {
            throw new IllegalArgumentException("Phone must have at least 10 digits");
        }
    }
}