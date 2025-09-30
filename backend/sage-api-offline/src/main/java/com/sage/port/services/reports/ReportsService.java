package com.sage.port.services.reports;

import com.sage.dto.v1.reports.response.AverageResolutionTimeResponseDto;
import com.sage.dto.v1.reports.response.AverageResponseTimeResponseDto;
import com.sage.dto.v1.reports.response.CriticalAssistsRateResponseDto;
import com.sage.dto.v1.reports.response.HourlyCallsResponseDto;
import com.sage.dto.v1.reports.response.TotalAssistsResponseDto;

import java.time.LocalDate;
import java.util.UUID;

public interface ReportsService {
    TotalAssistsResponseDto getTotalAssists(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    AverageResponseTimeResponseDto getAverageResponseTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    AverageResolutionTimeResponseDto getAverageResolutionTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    CriticalAssistsRateResponseDto getCriticalAssistsRate(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    HourlyCallsResponseDto getHourlyCallsByDay(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);
}
