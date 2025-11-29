package com.sage.controller.v1.resident;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.services.resident.ResidentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class ResidentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ResidentServiceImpl residentService;

    @InjectMocks
    private ResidentController residentController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UUID residentId;
    private Map<String, Object> sampleResident;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(residentController).build();
        residentId = UUID.randomUUID();

        sampleResident = new HashMap<>();
        sampleResident.put("id", residentId);
        sampleResident.put("fullName", "João da Silva");
        sampleResident.put("cpf", "12345678901");
        sampleResident.put("sex", 'M');
        sampleResident.put("residentialUnit", "A-101");
    }

    @Test
    void deveCriarResidenteComSucesso() throws Exception {
        CreateResidentRequestDto request = new CreateResidentRequestDto(
                "João da Silva",
                "12345678901",
                'M',
                ZonedDateTime.now(),
                "Maria",
                "11988887777",
                "Mãe",
                "A-101",
                42
        );

        when(residentService.createResident(
                anyString(), anyString(), anyChar(), any(), anyString(),
                anyString(), anyString(), anyString(), any()
        )).thenReturn(residentId);

        mockMvc.perform(post("/v1/resident")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().string("\"" + residentId.toString() + "\""));
    }

    @Test
    void deveAtualizarResidenteComSucesso() throws Exception {
        UpdateResidentRequestDto request = new UpdateResidentRequestDto(
                "João Atualizado",
                "12345678901",
                'M',
                ZonedDateTime.now(),
                "Maria Atualizada",
                "11999998888",
                "Irmã",
                "B-202",
                99
        );

        when(residentService.updateResident(
                any(), anyString(), anyString(), anyChar(), any(), anyString(),
                anyString(), anyString(), anyString(), any()
        )).thenReturn(sampleResident);

        mockMvc.perform(put("/v1/resident/{id}", residentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("João da Silva"));
    }

    @Test
    void deveListarResidentes() throws Exception {
        List<Map<String, Object>> residents = List.of(sampleResident);
        when(residentService.getResidents(anyInt(), anyInt(), any()))
                .thenReturn(residents);

        mockMvc.perform(get("/v1/resident")
                        .param("limit", "5")
                        .param("skip", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("João da Silva"));
    }

    @Test
    void deveBuscarDetalhesDoResidente() throws Exception {
        when(residentService.getResidentDetailsById(any()))
                .thenReturn(sampleResident);

        mockMvc.perform(get("/v1/resident/{id}", residentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    void deveFazerUploadDeImagem() throws Exception {
        MockMultipartFile image =
                new MockMultipartFile("imageData", "foto.png", "image/png", "fake-image".getBytes());

        doNothing().when(residentService).updateResidentImage(any(), any());

        mockMvc.perform(multipart("/v1/resident/{id}/image", residentId)
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarTotalDeResidentes() throws Exception {
        when(residentService.getCardTotal()).thenReturn(15L);

        mockMvc.perform(get("/v1/resident/card/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(15));
    }

    @Test
    void deveRetornarTotalDeAlertasAtivos() throws Exception {
        when(residentService.getTotalActiveAlerts()).thenReturn(5L);

        mockMvc.perform(get("/v1/resident/card/active-alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeAlerts").value(5));
    }

    @Test
    void deveRetornarTotalDeAlertasResolvidosHoje() throws Exception {
        when(residentService.getSolvedToday()).thenReturn(3L);

        mockMvc.perform(get("/v1/resident/card/solved-today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solvedToday").value(3));
    }

    @Test
    void deveRetornarTempoMedioDeAtendimento() throws Exception {
        when(residentService.getAssistMeanTime()).thenReturn("00:15:42");

        mockMvc.perform(get("/v1/resident/card/mean-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meanTimeAssist").value("00:15:42"));
    }
}