 package com.sage.integration.controller.resident;

 import java.util.List;
 import java.util.Map;
 import java.util.UUID;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import org.junit.jupiter.api.Test;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;

 import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
 import com.sage.integration.BaseTest;

 public class TestGetResidentByID extends BaseTest {

     @Test
     void testGetResidentById__notFoundResident__expect404() {
         // FIXTURE
         String residentId = UUID.randomUUID().toString();
         String url = "/api/v1/resident/" + residentId;

         // EXECUTION
         var response = restTemplate.getForEntity(url, String.class);

         // ASSERTION
         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
     }

     @Test
     void testGetResidentById__foundResident__expect200() {
         // FIXTURE
         super.insertSqlFile();
         List<Map<String, Object>> residents = super.list("resident");

         // ResidentDetailResponseDto[id=89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7, fullName=João Silva, cpf=12345678901, sex=M, birthDate=1950-07-15T00:00Z, createdAt=2025-05-01T10:00Z, updatedAt=2025-05-01T10:30Z, active=true, imageData=null, emergencyName=Maria Silva, emergencyPhone=11987651234, relationship=Filha, residentialUnit=A1, controlNumber=1]
         ResidentDetailResponseDto expectedResponse = new ResidentDetailResponseDto(
                 UUID.fromString("89ac8c08-e80b-4b68-b87e-e6aa6fcf60d7"),
                 "Joao Silva",
                 "12345678901",
                 'M',
                 java.time.ZonedDateTime.parse("1950-07-15T00:00Z"),
                 java.time.ZonedDateTime.parse("2025-05-01T10:00Z"),
                 java.time.ZonedDateTime.parse("2025-05-01T10:30Z"),
                 true,
                 null,
                 "Maria Silva",
                 "11987651234",
                 "Filha",
                 "A1",
                 1
         );

         String url = "/api/v1/resident/" + residents.get(0).get("id");
         // EXECUTION
         ResponseEntity<ResidentDetailResponseDto> response = restTemplate.getForEntity(url, ResidentDetailResponseDto.class);

         // ASSERTION
         assertEquals(HttpStatus.OK, response.getStatusCode());
         assertEquals(expectedResponse, response.getBody());
     }

 }
