package com.sage.controller.v1.alarm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.dto.v1.alarm.request.CreateAlarmRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmPortRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmRequestDto;
import com.sage.model.alarm.Alarm;
import com.sage.port.services.alarm.AlarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AlarmControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AlarmService alarmService;

    @InjectMocks
    private AlarmController alarmController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Alarm alarm;
    private UUID alarmId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(alarmController).build();
        alarmId = UUID.randomUUID();

        alarm = new Alarm();
        alarm.setId(alarmId);
        alarm.setModel("Active Full 32");
        alarm.setStatus("ACTIVE");
        alarm.setIpAddress("192.168.0.10");
        alarm.setMacAddress("AA:BB:CC:DD:EE:FF");
        alarm.setAccount("0001");
        alarm.setSerialNumber("2785040674");
        alarm.setPort(8080);
    }

    @Test
    void deveCriarAlarmeComSucesso() throws Exception {
        CreateAlarmRequestDto request = new CreateAlarmRequestDto(
                "Active Full 32",
                "ACTIVE",
                "192.168.0.10",
                "AA:BB:CC:DD:EE:FF",
                "0001",
                "2785040674",
                8080
        );

        when(alarmService.create(any(CreateAlarmRequestDto.class))).thenReturn(alarm);

        mockMvc.perform(post("/v1/alarms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Active Full 32"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void deveAtualizarAlarmePorId() throws Exception {
        UpdateAlarmRequestDto request = new UpdateAlarmRequestDto(
                "Active Full 32",
                "INACTIVE",
                "192.168.0.11",
                "AA:BB:CC:DD:EE:11",
                "0001",
                "2785040674"
        );

        when(alarmService.update(eq(alarmId), any(UpdateAlarmRequestDto.class))).thenReturn(alarm);

        mockMvc.perform(put("/v1/alarms/{id}", alarmId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Active Full 32"))
                .andExpect(jsonPath("$.serialNumber").value("2785040674"));
    }

    @Test
    void deveAtualizarAlarmePorNumeroDeSerie() throws Exception {
        UpdateAlarmRequestDto request = new UpdateAlarmRequestDto(
                "Active 20 2022",
                "ACTIVE",
                "192.168.0.12",
                "AA:BB:CC:22:EE:FF",
                "0001",
                "2785040674"
        );

        when(alarmService.updateBySerialNumber(eq("2785040674"), any(UpdateAlarmRequestDto.class)))
                .thenReturn(alarm);

        mockMvc.perform(put("/v1/alarms/serial/{serialNumber}", "2785040674")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("2785040674"));
    }

    @Test
    void deveAtualizarPortaPorNumeroDeSerie() throws Exception {
        UpdateAlarmPortRequestDto request = new UpdateAlarmPortRequestDto(9090);

        doNothing().when(alarmService).updatePortBySerialNumber(eq("2785040674"), any(UpdateAlarmPortRequestDto.class));

        mockMvc.perform(patch("/v1/alarms/serial/{serialNumber}/port", "2785040674")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveBuscarAlarmePorNumeroDeSerie() throws Exception {
        when(alarmService.getBySerialNumber("2785040674")).thenReturn(Optional.of(alarm));

        mockMvc.perform(get("/v1/alarms/serial/{serialNumber}", "2785040674"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Active Full 32"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void deveRetornar404SeAlarmeNaoForEncontradoPorSerial() throws Exception {
        when(alarmService.getBySerialNumber("2785040675")).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/alarms/serial/{serialNumber}", "2785040675"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarAlarmePorId() throws Exception {
        doNothing().when(alarmService).delete(alarmId);

        mockMvc.perform(delete("/v1/alarms/{id}", alarmId))
                .andExpect(status().isNoContent());
    }
}
