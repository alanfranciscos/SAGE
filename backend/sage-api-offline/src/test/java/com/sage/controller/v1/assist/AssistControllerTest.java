package com.sage.controller.v1.assist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.dto.v1.assist.request.CreateAssistRequestDto;
import com.sage.dto.v1.assist.request.FinishAssistRequestDto;
import com.sage.dto.v1.assist.request.StartAssistRequestDto;
import com.sage.dto.v1.assist.response.*;
import com.sage.port.services.assist.AssistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sage.model.assist.SeverityLevel;

@ExtendWith(MockitoExtension.class)
class AssistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssistService assistService;

    @InjectMocks
    private AssistController assistController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UUID assistId;
    private PaginatedPendingAssistResponseDto pendingResponse;
    private PaginatedAttendedAssistResponseDto attendedResponse;
    private PendingAssistDetailResponseDto pendingDetail;
    private AssistHistoryResponseDto historyResponse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(assistController).build();
        assistId = UUID.randomUUID();

        pendingResponse = new PaginatedPendingAssistResponseDto(List.of(), 1, 10, 0);
        attendedResponse = new PaginatedAttendedAssistResponseDto(List.of(), 2, 10, 0);
        pendingDetail = new PendingAssistDetailResponseDto(
                assistId, "Maria Souza", 84, "Bloco A", "00:05:23",
                SeverityLevel.WARNING, "pending", "base64Image"
        );
        historyResponse = new AssistHistoryResponseDto(
                assistId, "João Silva", 79, "Bloco B", "00:15:45",
                SeverityLevel.EMERGENCY, "finalizado", "Tudo bem agora", "base64Image"
        );
    }

    @Test
    void deveCriarAssistComSucesso() throws Exception {
        CreateAssistRequestDto request = new CreateAssistRequestDto(1, ZonedDateTime.now());
        when(assistService.createAssist(anyInt(), any())).thenReturn(assistId);

        mockMvc.perform(post("/v1/assist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/v1/assist/")));
    }

    @Test
    void deveRetornarListaDeAssistsPendentes() throws Exception {
        when(assistService.getPendingAssists(anyInt(), anyInt())).thenReturn(pendingResponse);

        mockMvc.perform(get("/v1/assist/pending?limit=5&skip=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.limit").value(10)); // default value
    }

    @Test
    void deveRetornarAssistPendentePorId() throws Exception {
        when(assistService.getPendingAssistById(eq(assistId))).thenReturn(pendingDetail);

        mockMvc.perform(get("/v1/assist/pending/{assistId}", assistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Maria Souza"))
                .andExpect(jsonPath("$.severityLevel").value("WARNING"));
    }

    @Test
    void deveRetornarListaDeAssistsAtendidos() throws Exception {
        when(assistService.getAttendedAssists(anyInt(), anyInt())).thenReturn(attendedResponse);

        mockMvc.perform(get("/v1/assist/history?limit=10&skip=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2));
    }

    @Test
    void deveRetornarHistoricoDeAssistPorId() throws Exception {
        when(assistService.getAssistHistoryById(eq(assistId))).thenReturn(historyResponse);

        mockMvc.perform(get("/v1/assist/history/{assistId}", assistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("João Silva"))
                .andExpect(jsonPath("$.status").value("finalizado"));
    }

    @Test
    void deveIniciarAssist() throws Exception {
        StartAssistRequestDto request = new StartAssistRequestDto();
        request.setCaregiverToken("token123");

        doNothing().when(assistService).startAssist(eq(assistId), eq("token123"));

        mockMvc.perform(patch("/v1/assist/{assistId}/start", assistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveFinalizarAssist() throws Exception {
        FinishAssistRequestDto request = new FinishAssistRequestDto();
        request.setCaregiverToken("token123");
        request.setDetails("Assist finalizado com sucesso.");

        doNothing().when(assistService)
                .finishAssist(eq(assistId), eq("token123"), eq("Assist finalizado com sucesso."));

        mockMvc.perform(patch("/v1/assist/{assistId}/finish", assistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}