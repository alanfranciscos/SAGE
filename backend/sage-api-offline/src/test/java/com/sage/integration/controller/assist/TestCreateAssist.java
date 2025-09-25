// package com.sage.integration.controller.assist;

// import java.time.ZoneId;
// import java.time.ZonedDateTime;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Test;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.sage.dto.v1.assist.request.CreateAssistRequestDto;
// import com.sage.integration.BaseTest;

// public class TestCreateAssist extends BaseTest {

//     // @Autowired
//     // ResidentDao residentDao;
//     // private void insertResident() {
//     //     Resident resident = new Resident(
//     //             UUID.randomUUID(),
//     //             "John Doe",
//     //             "12345678900",
//     //             'M',
//     //             ZonedDateTime.of(1990, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")),
//     //             ZonedDateTime.now(),
//     //             ZonedDateTime.now(),
//     //             "Unit 101",
//     //             null,
//     //             true
//     //     );
//     //     residentDao.createResident(resident);
//     // }
//     @Test
//     public void testCreateAssist__validControlId__expectSuccess() {
//         super.insertSqlFile();

//         CreateAssistRequestDto assistRequest = new CreateAssistRequestDto(
//                 1,
//                 ZonedDateTime.of(
//                         2011,
//                         4,
//                         25,
//                         0,
//                         0,
//                         0,
//                         0,
//                         ZoneId.of("UTC")
//                 )
//         );

//         ResponseEntity<String> response = restTemplate.postForEntity(
//                 "/api/v1/assist",
//                 assistRequest,
//                 String.class
//         );

//         assertEquals(HttpStatus.CREATED, response.getStatusCode());
//         // List<Map<String, Object>> assists = super.list("assist");
//         // assertEquals(1, assists.size());
//         // assertEquals(1, assists.get(0).get("control_id"));
//         // assertEquals("2011-04-25T00:00:00Z", assists.get(0).get("called_at").toString());

//     }
// }
