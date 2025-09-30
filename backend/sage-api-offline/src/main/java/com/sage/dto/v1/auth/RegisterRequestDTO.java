package com.sage.dto.v1.auth;

public record RegisterRequestDTO(String caregiver_password, String cpf, String email, String fullName, String imageData, String phone) {
}
