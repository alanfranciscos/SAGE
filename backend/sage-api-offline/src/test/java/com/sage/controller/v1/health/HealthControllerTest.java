package com.sage.controller.v1.health;

import com.sage.config.settings.Settings;
import com.sage.dto.v1.health.response.HealthResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest {

    private MockMvc mockMvc;
    private Settings settings;
    private static final String APP_VERSION = "1.0.0";

    @BeforeEach
    void setUp() {
        settings = Mockito.mock(Settings.class);
        Mockito.when(settings.getVersion()).thenReturn(APP_VERSION);

        HealthController healthController = new HealthController(settings);
        mockMvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    void deveRetornarStatusOkComInformacoesDeSaude() throws Exception {
        mockMvc.perform(get("/v1/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.uptime", containsString("s")))
                .andExpect(jsonPath("$.version").value(APP_VERSION))
                .andExpect(jsonPath("$.currentTime", not(emptyString())));
    }

    @Test
    void deveRetornarFormatoCorretoDoCampoCurrentTime() throws Exception {
        mockMvc.perform(get("/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T.*Z$")));
    }

    @Test
    void deveIncluirTodosOsCamposEsperados() throws Exception {
        mockMvc.perform(get("/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.uptime").exists())
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.currentTime").exists());
    }
}
