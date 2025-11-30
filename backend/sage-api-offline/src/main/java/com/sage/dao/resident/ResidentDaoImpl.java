package com.sage.dao.resident;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

@Repository
public class ResidentDaoImpl {

    private static final Logger logger = Logger.getLogger(ResidentDaoImpl.class.getName());

    final Connection connection;

    public ResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    private String toCamelCase(String snakeCase) {
        StringBuilder result = new StringBuilder();
        boolean upperNext = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else {
                if (upperNext) {
                    result.append(Character.toUpperCase(c));
                    upperNext = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
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
                + "       COALESCE(r.image_data, 'output-files/resident-image/default.png') AS image_data, "
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
                    String imagePath = resultSet.getString("image_data");
                    String base64Image = encodeFileToBase64(imagePath);
                    resident.put("imageData", base64Image);
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
                + "       COALESCE(r.image_data, 'output-files/resident-image/default.png') AS image_data, "
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
                List<Map<String, Object>> residents = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> resident = new HashMap<>();
                    resident.put("id", resultSet.getObject("id"));
                    String imagePath = resultSet.getString("image_data");
                    String base64Image = encodeFileToBase64(imagePath);
                    resident.put("imageData", base64Image);
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

    public Map<String, Object> getResidentDetailsById(UUID id) {
        String sql = "SELECT r.*, "
                + " rec.full_name AS emergency_full_name, rec.phone AS emergency_phone, rec.relationship AS emergency_relationship, "
                + " cr.control_id AS control_id "
                + " FROM resident r"
                + " LEFT JOIN resident_emergency_contact rec ON rec.resident_id = r.id "
                + " LEFT JOIN control_resident cr ON cr.resident_id = r.id "
                + " WHERE r.id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Map<String, Object> resident = new HashMap<>();

                    // Pegando metadata das colunas
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i); // ex: rec_full_name
                        String camelCaseName = toCamelCase(columnName); // ex: recFullName
                        Object columnValue = resultSet.getObject(i);

                        if ("imageData".equals(camelCaseName)) {
                            String imagePath = (String) columnValue;
                            if (imagePath == null) {
                                imagePath = "output-files/resident-image/default.png";
                            }
                            String base64Image = encodeFileToBase64(imagePath);
                            resident.put(camelCaseName, base64Image);
                        } else {
                            resident.put(camelCaseName, columnValue);
                        }
                    }

                    return resident;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching resident details by ID", e);
            throw new RuntimeException("Error fetching resident details by ID", e);
        }
    }

    public void insertResident(UUID residentId, String fullName, String cpf, char sex, ZonedDateTime birthDate, String residentialUnit) {
        String sql = "INSERT INTO resident (id, full_name, cpf, sex, birth_date, created_at, updated_at, residential_unit, active) "
                + "VALUES (?, ?, ?, ?, ?, NOW(), NOW(), ?, TRUE)";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setObject(1, residentId);
            ps.setString(2, fullName);
            ps.setString(3, cpf);
            ps.setString(4, String.valueOf(sex));
            ps.setObject(5, birthDate == null ? null : birthDate.toOffsetDateTime());
            ps.setString(6, residentialUnit);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inserting resident", e);
            throw new RuntimeException("Error inserting resident", e);
        }
    }

    public void insertResidentEmergencyContact(UUID emergencyId, UUID residentId, String emergencyName, String emergencyPhone, String relationship) {
        String sql = "INSERT INTO resident_emergency_contact (id, resident_id, full_name, phone, relationship) VALUES (?, ?, ?, ?, ?)";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setObject(1, emergencyId);
            ps.setObject(2, residentId);
            ps.setString(3, emergencyName);
            ps.setString(4, emergencyPhone);
            ps.setString(5, relationship);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inserting emergency contact", e);
            throw new RuntimeException("Error inserting emergency contact", e);
        }
    }

    public void insertControlResident(UUID controlId, int controlNumber, UUID alarmId, UUID residentId) {
        String sql = "INSERT INTO control_resident (id, control_id, alarm_id, resident_id) VALUES (?, ?, ?, ?)";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setObject(1, controlId);
            ps.setInt(2, controlNumber);
            ps.setObject(3, alarmId);
            ps.setObject(4, residentId);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inserting control_resident", e);
            throw new RuntimeException("Error inserting control_resident", e);
        }
    }

    public UUID getFirstAlarmId() {
        try (var stmt = connection.prepareStatement("SELECT id FROM alarm LIMIT 1")) {
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return (UUID) rs.getObject("id");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching alarm id", e);
            throw new RuntimeException("Error fetching alarm id", e);
        }
        throw new RuntimeException("No alarm found to associate with control_resident");
    }

    public Map<String, Object> updateResident(
            UUID id,
            String fullName,
            String cpf,
            char sex,
            ZonedDateTime birthDate,
            String emergencyName,
            String emergencyPhone,
            String relationship,
            String residentialUnit,
            Integer controlNumber
    ) {        // Atualiza dados do residente
        String updateResidentSql = "UPDATE resident SET full_name = ?, cpf = ?, sex = ?, birth_date = ?, updated_at = NOW(), residential_unit = ? WHERE id = ?";
        String updateEmergencySql = "UPDATE resident_emergency_contact SET full_name = ?, relationship = ?, phone = ? WHERE resident_id = ?";
        String updateControlSql = "UPDATE control_resident SET control_id = ? WHERE resident_id = ?";

        try {
            // Resident
            try (var ps = connection.prepareStatement(updateResidentSql)) {
                ps.setString(1, fullName);
                ps.setString(2, cpf);
                ps.setString(3, String.valueOf(sex));
                ps.setObject(4, birthDate == null ? null : birthDate.toOffsetDateTime());
                ps.setString(5, residentialUnit);
                ps.setObject(6, id);
                ps.executeUpdate();
            }

            // Emergency contact (só se algum campo for preenchido)
            if ((emergencyName != null && !emergencyName.isEmpty()) || (emergencyPhone != null && !emergencyPhone.isEmpty()) || (relationship != null && !relationship.isEmpty())) {
                try (var ps = connection.prepareStatement(updateEmergencySql)) {
                    ps.setString(1, emergencyName);
                    ps.setString(2, relationship);
                    ps.setString(3, emergencyPhone);
                    ps.setObject(4, id);
                    ps.executeUpdate();
                }
            }

            if (controlNumber != null) {
                try (var ps = connection.prepareStatement(updateControlSql)) {
                    ps.setObject(1, controlNumber);
                    ps.setObject(2, id);
                    ps.executeUpdate();
                }
            }

            return getResidentDetailsById(id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating resident", e);
            throw new RuntimeException("Error updating resident", e);
        }
    }

    public boolean existsByCpf(String cpf) {
        String sql = "SELECT EXISTS(SELECT 1 FROM resident WHERE cpf = ?)";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if resident exists by CPF", e);
            throw new RuntimeException("Error checking if resident exists by CPF", e);
        }
        return false;
    }

    public boolean existsByControlNumber(Integer controlNumber) {
        String sql = "SELECT EXISTS(SELECT 1 FROM control_resident WHERE control_id = ?)";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setInt(1, controlNumber);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if resident exists by control number", e);
            throw new RuntimeException("Error checking if resident exists by control number", e);
        }
        return false;
    }

    public void updateImageData(UUID residentId, String imageData) {
        String sql = "UPDATE resident SET image_data = ? WHERE id = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, imageData);
            ps.setObject(2, residentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating resident image data", e);
            throw new RuntimeException("Error updating resident image data", e);
        }
    }

    public void deactivateResident(UUID residentId) {
        String sql = "UPDATE resident SET active = FALSE, updated_at = NOW() WHERE id = ?";
        try (var ps = connection.prepareStatement(sql)) {
            ps.setObject(1, residentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deactivating resident", e);
            throw new RuntimeException("Error deactivating resident", e);
        }
    }

    private String encodeFileToBase64(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileContent = Files.readAllBytes(path);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            try {
                Path defaultPath = Paths.get("output-files/resident-image/default.png");
                byte[] fileContent = Files.readAllBytes(defaultPath);
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Could not read default image file", ex);
                return ""; // Or some other error indicator
            }
        }
    }
}
