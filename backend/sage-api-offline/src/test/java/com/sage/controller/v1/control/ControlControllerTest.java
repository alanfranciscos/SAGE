package com.sage.controller.v1.control;

import com.sage.dto.v1.control.response.ControlListResponseDto;
import com.sage.model.resident.control.ControlResident;
import com.sage.port.services.resident.ControlResidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ControlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ControlResidentService controlResidentService;

    @InjectMocks
    private ControlController controlController;

    private List<ControlResident> controlResidents;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controlController).build();

        ControlResident resident1 = new ControlResident();
        resident1.setId(UUID.randomUUID());
        resident1.setControlId(101);
        resident1.setResidentId(UUID.randomUUID());
        resident1.setAlarmId(UUID.randomUUID());

        ControlResident resident2 = new ControlResident();
        resident2.setId(UUID.randomUUID());
        resident2.setControlId(102);
        resident2.setResidentId(UUID.randomUUID());
        resident2.setAlarmId(UUID.randomUUID());

        controlResidents = List.of(resident1, resident2);
    }

    @Test
    void deveListarControlResidentsComSucesso() throws Exception {
        when(controlResidentService.listControl()).thenReturn(controlResidents);

        mockMvc.perform(get("/v1/control")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listControl").isArray())
                .andExpect(jsonPath("$.listControl.length()").value(2));

        verify(controlResidentService, times(1)).listControl();
    }
}