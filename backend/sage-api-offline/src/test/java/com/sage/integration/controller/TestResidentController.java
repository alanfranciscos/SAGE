package com.sage.integration.controller;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.integration.BaseTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class TestResidentController extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Map<String, String> insertBaseFixtures() {
        String alarm_Sql = "Insert into alarm (id, serial_number, count_number) values ";
        alarm_Sql += "('a1b2c3d4-e5f6-4789-a012-3456789abcde', '2785040674', 0)";

        String organization_sql = "Insert into organization ";
        organization_sql += "(id, alarm_id, full_name, cep, state, city, neighborhood, street, organization_number) values ";
        organization_sql += "('d4e5f6a7-b8c9-4012-d345-6789abcdef01','a1b2c3d4-e5f6-4789-a012-3456789abcde', 'Condominio Exemplo', '123456789', 'SP', 'São Paulo', 'Centro', 'Rua Exemplo', '101')";

        try {
            super.connection.prepareStatement(
                    alarm_Sql
            ).executeUpdate();
            super.connection.prepareStatement(
                    organization_sql
            ).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert base fixtures", e);
        }

        return new HashMap<>() {
            {
                put("alarm_id", "a1b2c3d4-e5f6-4789-a012-3456789abcde");
                put("organization_id", "d4e5f6a7-b8c9-4012-d345-6789abcdef01");
            }
        };
    }

    @Test
    void testCreateResident__validResident__expectResidentCreated() {
        // FIXTURE
        this.insertBaseFixtures();
        ZonedDateTime brithDay = ZonedDateTime.now().minusYears(30);

        Map<String, Object> expectedResident = new HashMap<>() {
            {
                put("id", null);
                put("full_name", "John Doe");
                put("cpf", "12345678901");
                put("sex", "M");
                put("residential_unit", "Unit 101");
                put("image_data", null);
                put("active", true);
                put("created_at", null);
                put("updated_at", null);
                put("birth_date", null);
            }
        };

        Map<String, Object> expectedEmergencyContact = new HashMap<>() {
            {
                put("full_name", "Jane Doe");
                put("phone", "987654321");
                put("relationship", "Sister");
                put("id", null);
                put("resident_id", null);
            }
        };

        Map<String, Object> expectedControlResident = new HashMap<>() {
            {
                put("control_id", 1);
                put("alarm_id", UUID.fromString("a1b2c3d4-e5f6-4789-a012-3456789abcde"));
                put("id", null);
                put("resident_id", null);
            }
        };

        CreateResidentRequestDto residentRequest = new CreateResidentRequestDto(
                "John Doe",
                "12345678901",
                'M',
                brithDay,
                Optional.of("Jane Doe"),
                Optional.of("987654321"),
                Optional.of("Sister"),
                "Unit 101",
                1,
                null
        );

        // EXERCISE
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/resident",
                residentRequest,
                String.class
        );

        // ASSERT
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String rawBody = response.getBody();
        String uuidString = rawBody != null ? rawBody.replace("\"", "") : "";

        List<Map<String, Object>> residents = super.list("resident");
        assertNotNull(residents);
        assertEquals(1, residents.size());

        Map<String, Object> residentCreated = residents.get(0);
        assertNotNull(residentCreated.get("created_at"));
        assertNotNull(residentCreated.get("updated_at"));
        assertNotNull(residentCreated.get("birth_date"));
        expectedResident.put("id", UUID.fromString(uuidString));
        expectedResident.put("created_at", residentCreated.get("created_at"));
        expectedResident.put("updated_at", residentCreated.get("updated_at"));
        expectedResident.put("birth_date", residentCreated.get("birth_date"));
        for (String key : residentCreated.keySet()) {
            assertEquals(expectedResident.get(key), residentCreated.get(key));
        }

        List<Map<String, Object>> residentEmergencyContact = super.list("resident_emergency_contact");
        assertNotNull(residentEmergencyContact);
        assertEquals(1, residentEmergencyContact.size());
        Map<String, Object> emergencyContactCreated = residentEmergencyContact.get(0);
        assertNotNull(emergencyContactCreated.get("id"));
        assertNotNull(emergencyContactCreated.get("resident_id"));
        expectedEmergencyContact.put("id", emergencyContactCreated.get("id"));
        expectedEmergencyContact.put("resident_id", residentCreated.get("id"));
        for (String key : emergencyContactCreated.keySet()) {
            assertEquals(expectedEmergencyContact.get(key), emergencyContactCreated.get(key));
        }

        List<Map<String, Object>> controlResidentCreated = super.list("control_resident");
        assertNotNull(controlResidentCreated);
        assertEquals(1, controlResidentCreated.size());
        Map<String, Object> controlResident = controlResidentCreated.get(0);
        assertNotNull(controlResident.get("id"));
        assertNotNull(controlResident.get("resident_id"));
        expectedControlResident.put("id", controlResident.get("id"));
        expectedControlResident.put("resident_id", residentCreated.get("id"));
        for (String key : controlResident.keySet()) {
            assertEquals(expectedControlResident.get(key), controlResident.get(key));
        }
    }
}
