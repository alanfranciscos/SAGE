package com.sage.services.reports;

import com.sage.dto.v1.reports.response.AverageResolutionTimeResponseDto;
import com.sage.dto.v1.reports.response.AverageResponseTimeResponseDto;
import com.sage.dto.v1.reports.response.CriticalAssistsRateResponseDto;
import com.sage.dto.v1.reports.response.TotalAssistsResponseDto;
import com.sage.port.dao.reports.ReportsDao;
import com.sage.port.services.reports.ReportsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ReportsServiceImpl implements ReportsService {

    private final ReportsDao reportsDao;

    public ReportsServiceImpl(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    @Override
    public TotalAssistsResponseDto getTotalAssists(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        long totalAssists = reportsDao.getTotalAssists(startDate, endDate, caregiverId, severity);
        return new TotalAssistsResponseDto(totalAssists);
    }

    @Override
    public AverageResponseTimeResponseDto getAverageResponseTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        String averageResponseTime = reportsDao.getAverageResponseTime(startDate, endDate, caregiverId, severity);
        return new AverageResponseTimeResponseDto(averageResponseTime);
    }

    @Override
    public AverageResolutionTimeResponseDto getAverageResolutionTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        String averageResolutionTime = reportsDao.getAverageResolutionTime(startDate, endDate, caregiverId, severity);
        return new AverageResolutionTimeResponseDto(averageResolutionTime);
    }

    @Override
    public CriticalAssistsRateResponseDto getCriticalAssistsRate(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        double criticalAssistsRate = reportsDao.getCriticalAssistsRate(startDate, endDate, caregiverId, severity);
        return new CriticalAssistsRateResponseDto(criticalAssistsRate);
    }
}
