package com.sage.dto.v1.caregiver.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateCaregiverRequestDto validation.
 */
public class CreateCaregiverRequestDtoTest {

    @Test
    public void testValidDto() {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
            "John Doe",
            "11987654321",
            "john.doe@example.com",
            "12345678901"
        );
        
        assertDoesNotThrow(() -> dto.validate(), "Valid DTO should not throw exception");
    }

    @Test
    public void testNullFullName() {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
            null,
            "11987654321",
            "john.doe@example.com",
            "12345678901"
        );
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, dto::validate);
        assertEquals("Full name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testEmptyFullName() {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
            "   ",
            "11987654321",
            "john.doe@example.com",
            "12345678901"
        );
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, dto::validate);
        assertEquals("Full name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testInvalidEmail() {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
            "John Doe",
            "11987654321",
            "invalid-email",
            "12345678901"
        );
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, dto::validate);
        assertEquals("Email must be a valid email address", exception.getMessage());
    }

    @Test
    public void testInvalidCpfLength() {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
            "John Doe",
            "11987654321",
            "john.doe@example.com",
            "123456789" // 9 digits instead of 11
        );
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, dto::validate);
        assertEquals("CPF must have 11 digits", exception.getMessage());
    }

    @Test
    public void testInvalidPhoneLength() {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
            "John Doe",
            "123", // Too short
            "john.doe@example.com",
            "12345678901"
        );
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, dto::validate);
        assertEquals("Phone must have at least 10 digits", exception.getMessage());
    }
}