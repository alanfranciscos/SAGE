package com.sage.integration.controller.control;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sage.dto.v1.control.response.ControlListResponseDto;
import com.sage.dto.v1.control.response.ControlResponseDto;
import com.sage.integration.BaseTest;

public class TestControl extends BaseTest {

    @Test
    void testListControl__withoutControlResidents__expectEmptyList() {
        // FIXTURE
        ControlListResponseDto expectedResponse = new ControlListResponseDto(
                new ArrayList<>()
        );

        // EXERCISE
        ResponseEntity<ControlListResponseDto> response = restTemplate.getForEntity(
                "/api/v1/control",
                ControlListResponseDto.class
        );

        // ASSERTION
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testListControl__withControlResidents__expectControlResidents() {
        // FIXTURE
        super.insertSqlFile();

        List<ControlResponseDto> controlResponseDtos = new ArrayList<>();
        controlResponseDtos.add(new ControlResponseDto(UUID.fromString("77777777-8888-4999-0000-111122223333"), 1, UUID.fromString("89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7")));
        controlResponseDtos.add(new ControlResponseDto(UUID.fromString("88888888-9999-4000-1111-222233334444"), 2, UUID.fromString("416f4967-89b1-49f8-a8d3-134c6e63cf5b")));
        controlResponseDtos.add(new ControlResponseDto(UUID.fromString("99999999-0000-4111-2222-333344445555"), 5, UUID.fromString("04d1f1c0-3022-4e53-9f34-d0136c89c9ed")));
        controlResponseDtos.add(new ControlResponseDto(UUID.fromString("00000000-1111-4222-3333-444455556666"), 3, UUID.fromString("55555555-6666-4777-8888-999000111222")));
        controlResponseDtos.add(new ControlResponseDto(UUID.fromString("11111111-2222-4333-4444-555566667777"), 4, UUID.fromString("66666666-7777-4888-9999-000111222333")));

        ControlListResponseDto expectedResponse = new ControlListResponseDto(
                controlResponseDtos
        );

        // EXERCISE
        ResponseEntity<ControlListResponseDto> response = restTemplate.getForEntity(
                "/api/v1/control",
                ControlListResponseDto.class
        );

        // ASSERTION
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Convert response body (JSON) to ControlListResponseDto for comparison

        assertEquals(expectedResponse, response.getBody());
    }
}
