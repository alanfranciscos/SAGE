package com.sage.controller.v1.reports;

import com.sage.dto.v1.reports.response.*;
import com.sage.port.services.reports.ReportsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReportsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReportsService reportsService;

    @InjectMocks
    private ReportsController reportsController;

    private UUID caregiverId;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();
        caregiverId = UUID.randomUUID();
    }

    @Test
    void deveRetornarTotalAssistsComSucesso() throws Exception {
        when(reportsService.getTotalAssists(any(), any(), any(), any()))
                .thenReturn(new TotalAssistsResponseDto(42L));

        mockMvc.perform(get("/v1/reports/total-assists")
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31")
                        .param("caregiverId", caregiverId.toString())
                        .param("severity", "emergency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAssists").value(42));

        verify(reportsService, times(1)).getTotalAssists(any(), any(), any(), any());
    }

    @Test
    void deveRetornarAverageResponseTimeComSucesso() throws Exception {
        when(reportsService.getAverageResponseTime(any(), any(), any(), any()))
                .thenReturn(new AverageResponseTimeResponseDto("00:05:00"));

        mockMvc.perform(get("/v1/reports/average-response-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageResponseTime").value("00:05:00"));
    }

    @Test
    void deveRetornarAverageResolutionTimeComSucesso() throws Exception {
        when(reportsService.getAverageResolutionTime(any(), any(), any(), any()))
                .thenReturn(new AverageResolutionTimeResponseDto("00:15:00"));

        mockMvc.perform(get("/v1/reports/average-resolution-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageResolutionTime").value("00:15:00"));
    }

    @Test
    void deveRetornarCriticalAssistsRateComSucesso() throws Exception {
        when(reportsService.getCriticalAssistsRate(any(), any(), any(), any()))
                .thenReturn(new CriticalAssistsRateResponseDto(12.5));

        mockMvc.perform(get("/v1/reports/critical-assists-rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.criticalAssistsRate").value(12.5));
    }

    @Test
    void deveRetornarHourlyCallsByDayComSucesso() throws Exception {
        when(reportsService.getHourlyCallsByDay(any(), any(), any(), any()))
                .thenReturn(new HourlyCallsResponseDto(Map.of(8, 3.0, 9, 5.0)));

        mockMvc.perform(get("/v1/reports/calls/hourly-by-day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hourlyAverage['8']").value(3.0))
                .andExpect(jsonPath("$.hourlyAverage['9']").value(5.0));
    }

    @Test
    void deveRetornarWeekdayCallsComSucesso() throws Exception {
        when(reportsService.getWeekdayCalls(any(), any(), any(), any()))
                .thenReturn(new WeekdayCallsResponseDto(Map.of("Monday", 10.0)));

        mockMvc.perform(get("/v1/reports/calls/weekday"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weekdayAverage.Monday").value(10.0));
    }

    @Test
    void deveRetornarTopFiveCallResidentsComSucesso() throws Exception {
        PerformanceMetricsDto metric = new PerformanceMetricsDto(5L, "00:10:00");
        when(reportsService.getTopFiveCallResidents(any(), any(), any(), any()))
                .thenReturn(Map.of("Resident A", metric));

        mockMvc.perform(get("/v1/reports/top-five/call-residents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Resident A'].quantity").value(5))
                .andExpect(jsonPath("$.['Resident A'].meanResponse").value("00:10:00"));
    }

    @Test
    void deveRetornarTopFiveCaregiverPerformanceComSucesso() throws Exception {
        PerformanceMetricsDto metric = new PerformanceMetricsDto(12L, "00:07:30");
        when(reportsService.getTopFiveCaregiverPerformance(any(), any(), any(), any()))
                .thenReturn(Map.of("Cuidador A", metric));

        mockMvc.perform(get("/v1/reports/top-five/caregiver-performance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Cuidador A'].quantity").value(12))
                .andExpect(jsonPath("$.['Cuidador A'].meanResponse").value("00:07:30"));
    }
}