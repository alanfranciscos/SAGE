package com.sage.dto.v1.auth;

public record ResetPasswordRequestDTO(String email, String token, String newPassword, String confirmPassword) {}

