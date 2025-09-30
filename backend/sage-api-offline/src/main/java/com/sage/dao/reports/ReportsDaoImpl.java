package com.sage.dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

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
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting critical assists rate", e);
        }
        return 0;
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
            params.add(severity);
        }
    }
}
