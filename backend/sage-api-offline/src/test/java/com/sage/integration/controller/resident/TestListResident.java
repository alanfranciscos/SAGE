package com.sage.integration.controller.resident;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sage.dto.v1.resident.response.ResidentHeaderResponseDto;
import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.integration.BaseTest;

public class TestListResident extends BaseTest {

    @Test
    void tesListResidents__withoutResidents__expectEmptyResidents() {
        // FIXTURE
        ResidentListResponseDto expectedResponse = new ResidentListResponseDto(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                0L,
                10L,
                0L
        );

        // EXERCISE
        ResponseEntity<ResidentListResponseDto> response = restTemplate.getForEntity(
                "/api/v1/resident",
                ResidentListResponseDto.class
        );

        //ASSERT
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testListResidents__withResidents__expectResidents() {
        // FIXTURE

        super.insertSqlFile();
        // ResidentListResponseDto@117 "ResidentListResponseDto[severalResidents=[ResidentHeaderResponseDto[id=66666666-7777-4888-9999-000111222333, fullName=Pedro Almeida, residentialUnit=E5, imageData=[B@5fafd099], ResidentHeaderResponseDto[id=55555555-6666-4777-8888-999000111222, fullName=Helena Fernandes, residentialUnit=D4, imageData=[B@580e01cd]], warningResidents=[ResidentHeaderResponseDto[id=99998888-7777-4888-9999-000111222333, fullName=Teste warning, residentialUnit=E5, imageData=[B@1e9a10e9]], normalResidents=[ResidentHeaderResponseDto[id=89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7, fullName=João Silva, residentialUnit=A1, imageData=[B@233d2b76], ResidentHeaderResponseDto[id=416f4967-89b1-49f8-a8d3-134c6e63cf5b, fullName=Luana Costa, residentialUnit=B2, imageData=[B@2370c90], ResidentHeaderResponseDto[id=04d1f1c0-3022-4e53-9f34-d0136c89c9ed, fullName=Ricardo Gomes, residentialUnit=C3, imageData=[B@2a9f7572], ResidentHeaderResponseDto[id=99999999-7777-4888-9999-000111222333, fullName=Teste normal, residentialUnit=E5, imageData=[B@644c3590]], totalResidents=7, limit=7, skip=0]
        ArrayList<ResidentHeaderResponseDto> severalResidents = new ArrayList<>();
        severalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("66666666-7777-4888-9999-000111222333"),
                "Pedro Almeida",
                "E5",
                null
        ));
        severalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("55555555-6666-4777-8888-999000111222"),
                "Helena Fernandes",
                "D4",
                null
        ));

        ArrayList<ResidentHeaderResponseDto> warningResidents = new ArrayList<>();
        warningResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("99998888-7777-4888-9999-000111222333"),
                "Teste warning",
                "E5",
                null
        ));

        ArrayList<ResidentHeaderResponseDto> normalResidents = new ArrayList<>();
        normalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7"),
                "Joao Silva",
                "A1",
                null
        ));
        normalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("416f4967-89b1-49f8-a8d3-134c6e63cf5b"),
                "Luana Costa",
                "B2",
                null
        ));
        normalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("04d1f1c0-3022-4e53-9f34-d0136c89c9ed"),
                "Ricardo Gomes",
                "C3",
                null
        ));
        normalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("99999999-7777-4888-9999-000111222333"),
                "Teste normal",
                "E5",
                null
        ));

        ResidentListResponseDto expectedResponse = new ResidentListResponseDto(
                severalResidents,
                warningResidents,
                normalResidents,
                7L,
                7L,
                0L
        );
        // EXERCISE
        ResponseEntity<ResidentListResponseDto> response = restTemplate.getForEntity(
                "/api/v1/resident",
                ResidentListResponseDto.class
        );

        // ASSERT
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testListResidents__searchResident__expectResidents() {
        // FIXTURE

        super.insertSqlFile();
        // ResidentListResponseDto@117 "ResidentListResponseDto[severalResidents=[ResidentHeaderResponseDto[id=66666666-7777-4888-9999-000111222333, fullName=Pedro Almeida, residentialUnit=E5, imageData=[B@5fafd099], ResidentHeaderResponseDto[id=55555555-6666-4777-8888-999000111222, fullName=Helena Fernandes, residentialUnit=D4, imageData=[B@580e01cd]], warningResidents=[ResidentHeaderResponseDto[id=99998888-7777-4888-9999-000111222333, fullName=Teste warning, residentialUnit=E5, imageData=[B@1e9a10e9]], normalResidents=[ResidentHeaderResponseDto[id=89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7, fullName=João Silva, residentialUnit=A1, imageData=[B@233d2b76], ResidentHeaderResponseDto[id=416f4967-89b1-49f8-a8d3-134c6e63cf5b, fullName=Luana Costa, residentialUnit=B2, imageData=[B@2370c90], ResidentHeaderResponseDto[id=04d1f1c0-3022-4e53-9f34-d0136c89c9ed, fullName=Ricardo Gomes, residentialUnit=C3, imageData=[B@2a9f7572], ResidentHeaderResponseDto[id=99999999-7777-4888-9999-000111222333, fullName=Teste normal, residentialUnit=E5, imageData=[B@644c3590]], totalResidents=7, limit=7, skip=0]
        ArrayList<ResidentHeaderResponseDto> severalResidents = new ArrayList<>();
        severalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("66666666-7777-4888-9999-000111222333"),
                "Pedro Almeida",
                "E5",
                null
        ));
        severalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("55555555-6666-4777-8888-999000111222"),
                "Helena Fernandes",
                "D4",
                null
        ));

        ArrayList<ResidentHeaderResponseDto> warningResidents = new ArrayList<>();
        warningResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("99998888-7777-4888-9999-000111222333"),
                "Teste warning",
                "E5",
                null
        ));

        ArrayList<ResidentHeaderResponseDto> normalResidents = new ArrayList<>();
        normalResidents.add(new ResidentHeaderResponseDto(
                UUID.fromString("04d1f1c0-3022-4e53-9f34-d0136c89c9ed"),
                "Ricardo Gomes",
                "C3",
                null
        ));

        ResidentListResponseDto expectedResponse = new ResidentListResponseDto(
                severalResidents,
                warningResidents,
                normalResidents,
                7L,
                7L,
                0L
        );
        // EXERCISE
        ResponseEntity<ResidentListResponseDto> response = restTemplate.getForEntity(
                "/api/v1/resident?search=Ricardo",
                ResidentListResponseDto.class
        );

        // ASSERT
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}
