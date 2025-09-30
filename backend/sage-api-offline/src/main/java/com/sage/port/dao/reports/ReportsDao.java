package com.sage.port.dao.reports;

import java.time.LocalDate;
import java.util.UUID;

public interface ReportsDao {
    long getTotalAssists(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    String getAverageResponseTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    String getAverageResolutionTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);

    double getCriticalAssistsRate(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity);
}
