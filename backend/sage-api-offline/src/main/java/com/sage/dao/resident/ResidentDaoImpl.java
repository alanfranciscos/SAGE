package com.sage.dao.resident;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

@Repository
public class ResidentDaoImpl {

    private static final Logger logger = Logger.getLogger(ResidentDaoImpl.class.getName());

    private final Connection connection;

    public ResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Long getCardTotal() {
        String sql = "SELECT COUNT(*) FROM resident";
        try (var preparedStatement = connection.prepareStatement(sql); var resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return 0L;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error counting residents", e);
            throw new RuntimeException("Error counting residents", e);
        }
    }

    public List<Map<String, Object>> getResidentsWithoutSeverity(
            int limit,
            int skip,
            String search
    ) {
        String sql = "SELECT r.id, "
                + "       r.full_name, "
                + "       r.residential_unit, "
                + "       r.image_data, "
                + "       MAX(a.end_at) AS last_end_at, "
                + "       COUNT(a.id) FILTER (WHERE a.called_at >= NOW() - INTERVAL '24 hours') AS calls_last_day "
                + "FROM resident r "
                + "LEFT JOIN assist a ON a.resident_id = r.id "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 "
                + "    FROM assist a2 "
                + "    WHERE a2.resident_id = r.id "
                + "      AND a2.end_at IS NULL "
                + ") "
                + "GROUP BY r.id, r.full_name, r.residential_unit, r.image_data";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (r.full_name LIKE ? OR r.cpf LIKE ?)";
        }

        sql += " ORDER BY r.full_name ASC LIMIT ? OFFSET ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + search + "%");
                preparedStatement.setString(paramIndex++, "%" + search + "%");
            }
            preparedStatement.setInt(paramIndex++, limit);
            preparedStatement.setInt(paramIndex, skip);

            try (var resultSet = preparedStatement.executeQuery()) {
                List<Map<String, Object>> residents = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> resident = new java.util.HashMap<>();
                    resident.put("id", resultSet.getObject("id"));
                    resident.put("imageData", resultSet.getString("image_data"));
                    resident.put("fullName", resultSet.getString("full_name"));
                    resident.put("lastEndAt", resultSet.getString("last_end_at"));
                    resident.put("residentialUnit", resultSet.getString("residential_unit"));
                    resident.put("callsLastDay", resultSet.getInt("calls_last_day"));
                    resident.put("severityLevel", null);
                    residents.add(resident);
                }
                return residents;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing residents without severity: {0}", e.getMessage());
            throw new RuntimeException("Error listing residents without severity", e);
        }

    }

    public List<Map<String, Object>> getResidentsWithSeverity(int limit, int skip, String search) {
        String sql
                = "SELECT r.id, "
                + "       r.full_name, "
                + "       r.residential_unit, "
                + "       r.image_data, "
                + "       a.id AS assist_id, "
                + "       a.called_at, "
                + "       a.severity_level, "
                + "       a.detail, "
                + "       ( "
                + "           SELECT MAX(a2.end_at) "
                + "           FROM assist a2 "
                + "           WHERE a2.resident_id = r.id "
                + "             AND a2.end_at IS NOT NULL "
                + "       ) AS last_end_at, "
                + "       ( "
                + "           SELECT COUNT(*) "
                + "           FROM assist a3 "
                + "           WHERE a3.resident_id = r.id "
                + "             AND a3.called_at >= NOW() - INTERVAL '24 hours' "
                + "       ) AS calls_last_day "
                + "FROM resident r "
                + "INNER JOIN assist a ON a.resident_id = r.id "
                + "WHERE a.end_at IS NULL ";

        if (search != null && !search.trim().isEmpty()) {
            sql += " AND (r.full_name LIKE ? OR r.cpf LIKE ?)";
        }
        sql += " ORDER BY r.full_name ASC LIMIT ? OFFSET ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            if (search != null && !search.trim().isEmpty()) {
                preparedStatement.setString(1, "%" + search + "%");
                preparedStatement.setString(2, "%" + search + "%");
                preparedStatement.setInt(3, limit);
                preparedStatement.setInt(4, skip);
            } else {
                preparedStatement.setInt(1, limit);
                preparedStatement.setInt(2, skip);
            }

            try (var resultSet = preparedStatement.executeQuery()) {
                List<Map<String, Object>> residents = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> resident = new java.util.HashMap<>();
                    resident.put("id", resultSet.getObject("id"));
                    resident.put("imageData", resultSet.getString("image_data"));
                    resident.put("fullName", resultSet.getString("full_name"));
                    resident.put("lastEndAt", resultSet.getString("last_end_at"));
                    resident.put("residentialUnit", resultSet.getString("residential_unit"));
                    resident.put("callsLastDay", resultSet.getInt("calls_last_day"));
                    resident.put("severityLevel", resultSet.getString("severity_level"));
                    residents.add(resident);
                }
                return residents;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing residents: {0}", e.getMessage());
            throw new RuntimeException("Error listing residents", e);
        }
    }
}
