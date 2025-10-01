package com.sage.dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sage.dto.v1.reports.response.PerformanceMetricsDto;
import com.sage.port.dao.reports.ReportsDao;

@Repository
public class ReportsDaoImpl implements ReportsDao {

    private final Connection connection;

    public ReportsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long getTotalAssists(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM assist a WHERE 1=1");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total assists", e);
        }
        return 0;
    }

    @Override
    public String getAverageResponseTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT AVG(assignment_at - called_at) FROM assist a WHERE assignment_at IS NOT NULL");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting average response time", e);
        }
        return null;
    }

    @Override
    public String getAverageResolutionTime(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT AVG(end_at - called_at) FROM assist a WHERE end_at IS NOT NULL");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting average resolution time", e);
        }
        return null;
    }

    @Override
    public double getCriticalAssistsRate(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT (COUNT(CASE WHEN severity_level = 'emergency' THEN 1 END) * 100.0) / COUNT(*) FROM assist a WHERE 1=1");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 100.0) / 100.0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting critical assists rate", e);
        }
        return 0;
    }

    @Override
    public Map<Integer, Double> getHourlyCallsByDay(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT EXTRACT(HOUR FROM a.called_at) AS hour, COUNT(*) AS total_calls FROM assist a WHERE 1=1");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        sql.append(" GROUP BY hour ORDER BY hour");

        Map<Integer, Double> hourlyCalls = new HashMap<>();
        long numberOfDays = 1;
        if (startDate != null && endDate != null) {
            numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int hour = rs.getInt("hour");
                    long totalCalls = rs.getLong("total_calls");
                    double average = (double) totalCalls / numberOfDays;
                    hourlyCalls.put(hour, average);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting hourly calls by day", e);
        }

        return hourlyCalls;
    }

    @Override
    public Map<String, Double> getWeekdayCalls(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT TO_CHAR(a.called_at, 'Day') AS weekday, COUNT(*) AS total_calls FROM assist a WHERE 1=1");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        sql.append(" GROUP BY weekday ORDER BY weekday");

        Map<String, Double> weekdayCalls = new HashMap<>();
        long numberOfDays = 1;
        if (startDate != null && endDate != null) {
            numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        double numberOfWeeks = Math.max(1.0, numberOfDays / 7.0);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String weekday = rs.getString("weekday").trim();
                    long totalCalls = rs.getLong("total_calls");
                    double average = (double) totalCalls / numberOfWeeks;
                    weekdayCalls.put(weekday, average);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting weekday calls", e);
        }

        return weekdayCalls;
    }

    @Override
    public Map<String, PerformanceMetricsDto> getTopFiveCallResidents(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT r.full_name, COUNT(a.id) AS quantity, AVG(a.end_at - a.called_at) AS mean_response FROM assist a JOIN resident r ON a.resident_id = r.id WHERE 1=1");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        sql.append(" GROUP BY r.full_name ORDER BY quantity DESC LIMIT 5");

        Map<String, PerformanceMetricsDto> result = new LinkedHashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String residentName = rs.getString("full_name");
                    long quantity = rs.getLong("quantity");
                    String meanResponse = rs.getString("mean_response");
                    result.put(residentName, new PerformanceMetricsDto(quantity, meanResponse));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting top five call residents", e);
        }

        return result;
    }

    @Override
    public Map<String, PerformanceMetricsDto> getTopFiveCaregiverPerformance(LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        StringBuilder sql = new StringBuilder("SELECT c.full_name, COUNT(a.id) AS quantity, AVG(a.end_at - a.called_at) AS mean_response FROM assist a JOIN caregiver c ON a.caregiver_id = c.id WHERE a.caregiver_id IS NOT NULL");
        List<Object> params = new ArrayList<>();

        applyFilters(sql, params, startDate, endDate, caregiverId, severity);

        sql.append(" GROUP BY c.full_name ORDER BY quantity DESC LIMIT 5");

        Map<String, PerformanceMetricsDto> result = new LinkedHashMap<>();

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String caregiverName = rs.getString("full_name");
                    long quantity = rs.getLong("quantity");
                    String meanResponse = rs.getString("mean_response");
                    result.put(caregiverName, new PerformanceMetricsDto(quantity, meanResponse));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting top five caregiver performance", e);
        }

        return result;
    }

    private void applyFilters(StringBuilder sql, List<Object> params, LocalDate startDate, LocalDate endDate, UUID caregiverId, String severity) {
        if (startDate != null) {
            sql.append(" AND a.called_at >= ?");
            params.add(startDate.atStartOfDay());
        }
        if (endDate != null) {
            sql.append(" AND a.called_at <= ?");
            params.add(endDate.atTime(23, 59, 59));
        }
        if (caregiverId != null) {
            sql.append(" AND a.caregiver_id = ?");
            params.add(caregiverId);
        }
        if (severity != null && !severity.isEmpty()) {
            sql.append(" AND a.severity_level = ?");
            params.add(severity.toUpperCase());
        }
    }
}
