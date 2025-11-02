package com.sage.controller.v1.reports;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sage.dto.v1.reports.response.AverageResolutionTimeResponseDto;
import com.sage.dto.v1.reports.response.AverageResponseTimeResponseDto;
import com.sage.dto.v1.reports.response.CriticalAssistsRateResponseDto;
import com.sage.dto.v1.reports.response.HourlyCallsResponseDto;
import com.sage.dto.v1.reports.response.PerformanceMetricsDto;
import com.sage.dto.v1.reports.response.TotalAssistsResponseDto;
import com.sage.dto.v1.reports.response.WeekdayCallsResponseDto;
import com.sage.port.services.reports.ReportsService;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/total-assists")
    public ResponseEntity<TotalAssistsResponseDto> getTotalAssists(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        TotalAssistsResponseDto result = reportsService.getTotalAssists(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/average-response-time")
    public ResponseEntity<AverageResponseTimeResponseDto> getAverageResponseTime(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        AverageResponseTimeResponseDto result = reportsService.getAverageResponseTime(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/average-resolution-time")
    public ResponseEntity<AverageResolutionTimeResponseDto> getAverageResolutionTime(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        AverageResolutionTimeResponseDto result = reportsService.getAverageResolutionTime(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/critical-assists-rate")
    public ResponseEntity<CriticalAssistsRateResponseDto> getCriticalAssistsRate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        CriticalAssistsRateResponseDto result = reportsService.getCriticalAssistsRate(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/calls/hourly-by-day")
    public ResponseEntity<HourlyCallsResponseDto> getHourlyCallsByDay(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        HourlyCallsResponseDto result = reportsService.getHourlyCallsByDay(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/calls/weekday")
    public ResponseEntity<WeekdayCallsResponseDto> getWeekdayCalls(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        WeekdayCallsResponseDto result = reportsService.getWeekdayCalls(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top-five/call-residents")
    public ResponseEntity<Map<String, PerformanceMetricsDto>> getTopFiveCallResidents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        Map<String, PerformanceMetricsDto> result = reportsService.getTopFiveCallResidents(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top-five/caregiver-performance")
    public ResponseEntity<Map<String, PerformanceMetricsDto>> getTopFiveCaregiverPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID caregiverId,
            @RequestParam(required = false) String severity) {
        Map<String, PerformanceMetricsDto> result = reportsService.getTopFiveCaregiverPerformance(startDate, endDate, caregiverId, severity);
        return ResponseEntity.ok(result);
    }
}
