package com.sage.controller.v1.sse;

import com.sage.port.services.sse.AssistSseService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SseAssistControllerTest {

    private MockMvc mockMvc;
    private AssistSseService assistSseService;

    @BeforeEach
    void setUp() {
        assistSseService = Mockito.mock(AssistSseService.class);
        SseAssistController controller = new SseAssistController(assistSseService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void deveRetornarSseEmitterQuandoSolicitado() throws Exception {
        SseEmitter mockEmitter = new SseEmitter();
        when(assistSseService.listEvent()).thenReturn(mockEmitter);

        mockMvc.perform(get("/v1/sse/assist")
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted());
        
        verify(assistSseService, times(1)).listEvent();
    }

    @Test
    void deveLancarErroSeServicoFalhar() {
        when(assistSseService.listEvent()).thenThrow(new RuntimeException("Falha na conexão SSE"));

        Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/v1/sse/assist")
                            .accept(MediaType.TEXT_EVENT_STREAM));
        });

        verify(assistSseService, times(1)).listEvent();
    }
}