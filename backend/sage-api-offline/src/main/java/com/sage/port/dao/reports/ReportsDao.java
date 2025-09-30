package com.sage.port.dao.reports;

import com.sage.dto.v1.reports.response.PerformanceMetricsDto;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface ReportsDao {
    long getTotalAssists(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    String getAverageResponseTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    String getAverageResolutionTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    double getCriticalAssistsRate(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    Map<Integer, Double> getHourlyCallsByDay(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    Map<String, Double> getWeekdayCalls(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    Map<String, PerformanceMetricsDto> getTopFiveCallResidents(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    Map<String, PerformanceMetricsDto> getTopFiveCaregiverPerformance(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);
}
