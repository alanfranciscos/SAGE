package com.sage.dto.v1.caregiver.request;

import com.sage.exception.InvalidInputException;

public record CreateCaregiverRequestDto(
        String fullName,
        String cpf,
        String email,
        String phone,
        String position
        ) {

    public void validate() {
        if (fullName == null || fullName.isBlank()) {
            throw new InvalidInputException("Full name is required.");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidInputException("CPF is required.");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Email is required.");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidInputException("Phone is required.");
        }
        if (position == null || position.isBlank()) {
            throw new InvalidInputException("Position is required.");
        }
        if (!position.equals("employee") && !position.equals("chieff")) {
            throw new InvalidInputException("Position must be 'employee' or 'chieff'.");
        }
    }
}
