package com.sage.dao.assist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.dto.v1.assist.response.AssistHistoryResponseDto;
import com.sage.dto.v1.assist.response.AttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.dto.v1.assist.response.PendingAssistDetailResponseDto;
import com.sage.dto.v1.assist.response.PendingAssistResponseDto;
import com.sage.model.assist.Assist;
import com.sage.model.assist.SeverityLevel;
import com.sage.port.dao.assist.AssistDao;

public class AssistDaoImpl implements AssistDao {

    private static final Logger logger = Logger.getLogger(AssistDaoImpl.class.getName());

    private final Connection connection;

    public AssistDaoImpl(Connection connection) {
        this.connection = connection;
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

    private PreparedStatement prepareParametersToSave(
            PreparedStatement preparedStatement,
            Assist entity
    ) throws SQLException {

        preparedStatement.setObject(1, entity.getCaregiverId());
        preparedStatement.setObject(2, entity.getResidentId());

        preparedStatement.setObject(3, entity.getCalledAt() != null ? entity.getCalledAt().toOffsetDateTime() : null);
        preparedStatement.setObject(4, entity.getAssignmentAt() != null ? entity.getAssignmentAt().toOffsetDateTime() : null);
        preparedStatement.setObject(5, entity.getEndAt() != null ? entity.getEndAt().toOffsetDateTime() : null);

        preparedStatement.setString(6, entity.getDetail());
        preparedStatement.setString(7, entity.getSeverityLevel().getValue());

        return preparedStatement;
    }

    @Override
    public UUID create(Assist assist) {
        String sql = "INSERT INTO assist";
        sql += " (caregiver_id, resident_id, called_at, assignment_at, end_at, detail, severity_level)";
        sql += " VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement = this.prepareParametersToSave(preparedStatement, assist);
            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                final UUID id = (UUID) resultSet.getObject(1);
                assist.setId(id);
            }

            connection.commit();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(
                        Level.SEVERE,
                        "Error rolling back transaction: {0}", rollbackEx.getMessage()
                );
                throw new RuntimeException("Error rolling back transaction: " + rollbackEx.getMessage(), rollbackEx);
            }
            logger.log(
                    Level.SEVERE,
                    "Error saving Assist: {0}", e.getMessage()
            );
            throw new RuntimeException("Error saving Assist: " + e.getMessage(), e);
        }

        return assist.getId();
    }

    @Override
    public Optional<Assist> findByResidentIdAndEndAtIsNull(UUID residentId) {
        String sql = "SELECT * FROM assist WHERE resident_id = ? AND end_at IS NULL";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, residentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Assist.mapFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding Assist: {0}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public UUID update(Assist assist) {
        String sql = "UPDATE assist SET caregiver_id = ?, resident_id = ?, called_at = ?, assignment_at = ?, end_at = ?, detail = ?, severity_level = ? WHERE id = ?";

        PreparedStatement preparedStatement;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement = this.prepareParametersToSave(preparedStatement, assist);
            preparedStatement.setObject(8, assist.getId());
            preparedStatement.executeUpdate();

            connection.commit();

            preparedStatement.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(
                        Level.SEVERE,
                        "Error rolling back transaction: {0}", rollbackEx.getMessage()
                );
                throw new RuntimeException("Error rolling back transaction: " + rollbackEx.getMessage(), rollbackEx);
            }
            logger.log(
                    Level.SEVERE,
                    "Error updating Assist: {0}", e.getMessage()
            );
            throw new RuntimeException("Error updating Assist: " + e.getMessage(), e);
        }

        return assist.getId();
    }

    @Override
    public PaginatedPendingAssistResponseDto getPendingAssists(int limit, int skip, String search) {
        String baseWhere = "WHERE a.end_at IS NULL";
        String searchCondition = "";
        
        if (search != null && !search.trim().isEmpty()) {
            searchCondition = " AND (r.full_name ILIKE ? OR r.cpf LIKE ?)";
        }

        String countSql = "SELECT COUNT(*) FROM assist a JOIN resident r ON a.resident_id = r.id " + baseWhere + searchCondition;
        long total = 0;

        try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
            if (!searchCondition.isEmpty()) {
                String searchTerm = "%" + search + "%";
                countStatement.setString(1, searchTerm);
                countStatement.setString(2, searchTerm);
            }
            try (ResultSet resultSet = countStatement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error counting pending assists: {0}", e.getMessage());
            throw new RuntimeException("Error counting pending assists: " + e.getMessage(), e);
        }

        String sql = "SELECT "
                + "a.id, "
                + "r.full_name, "
                + "r.residential_unit, "
                + "COALESCE(r.image_data, 'output-files/resident-image/default.png') AS image_data, "
                + "NOW() - a.called_at AS elapsed_time, "
                + "a.severity_level, "
                + "CASE WHEN a.assignment_at IS NULL THEN 'pending' ELSE 'in_attendance' END AS status "
                + "FROM assist a "
                + "JOIN resident r ON a.resident_id = r.id "
                + baseWhere + searchCondition
                + " ORDER BY CASE a.severity_level WHEN 'EMERGENCY' THEN 1 WHEN 'WARNING' THEN 2 ELSE 3 END DESC "
                + "LIMIT ? OFFSET ?";

        List<PendingAssistResponseDto> pendingAssists = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (!searchCondition.isEmpty()) {
                String searchTerm = "%" + search + "%";
                preparedStatement.setString(paramIndex++, searchTerm);
                preparedStatement.setString(paramIndex++, searchTerm);
            }
            preparedStatement.setInt(paramIndex++, limit);
            preparedStatement.setInt(paramIndex++, skip);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String imagePath = resultSet.getString("image_data");
                    String base64Image = encodeFileToBase64(imagePath);
                    pendingAssists.add(new PendingAssistResponseDto(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("full_name"),
                            resultSet.getString("residential_unit"),
                            resultSet.getString("elapsed_time").split("\\.")[0],
                            SeverityLevel.fromValue(resultSet.getString("severity_level")),
                            resultSet.getString("status"),
                            base64Image
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting pending assists: {0}", e.getMessage());
            throw new RuntimeException("Error getting pending assists: " + e.getMessage(), e);
        }

        return new PaginatedPendingAssistResponseDto(pendingAssists, total, limit, skip);
    }

    @Override
    public PaginatedAttendedAssistResponseDto getAttendedAssists(int limit, int skip, String search) {
        String baseWhere = "WHERE a.end_at IS NOT NULL";
        String searchCondition = "";

        if (search != null && !search.trim().isEmpty()) {
            searchCondition = " AND (r.full_name ILIKE ? OR r.cpf LIKE ?)";
        }

        String countSql = "SELECT COUNT(*) FROM assist a JOIN resident r ON a.resident_id = r.id " + baseWhere + searchCondition;
        long total = 0;

        try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
            if (!searchCondition.isEmpty()) {
                String searchTerm = "%" + search + "%";
                countStatement.setString(1, searchTerm);
                countStatement.setString(2, searchTerm);
            }
            try (ResultSet resultSet = countStatement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error counting attended assists: {0}", e.getMessage());
            throw new RuntimeException("Error counting attended assists: " + e.getMessage(), e);
        }

        String sql = "SELECT "
                + "a.id, "
                + "r.full_name, "
                + "r.residential_unit, "
                + "COALESCE(r.image_data, 'output-files/resident-image/default.png') AS image_data, "
                + "a.end_at - a.called_at AS elapsed_time, "
                + "a.detail, "
                + "a.severity_level "
                + "FROM assist a "
                + "JOIN resident r ON a.resident_id = r.id "
                + baseWhere + searchCondition
                + " ORDER BY a.end_at DESC "
                + "LIMIT ? OFFSET ?";

        List<AttendedAssistResponseDto> attendedAssists = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            if (!searchCondition.isEmpty()) {
                String searchTerm = "%" + search + "%";
                preparedStatement.setString(paramIndex++, searchTerm);
                preparedStatement.setString(paramIndex++, searchTerm);
            }
            preparedStatement.setInt(paramIndex++, limit);
            preparedStatement.setInt(paramIndex++, skip);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String imagePath = resultSet.getString("image_data");
                    String base64Image = encodeFileToBase64(imagePath);
                    attendedAssists.add(new AttendedAssistResponseDto(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("full_name"),
                            resultSet.getString("residential_unit"),
                            resultSet.getString("elapsed_time"),
                            resultSet.getString("detail"),
                            SeverityLevel.fromValue(resultSet.getString("severity_level")),
                            base64Image
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting attended assists: {0}", e.getMessage());
            throw new RuntimeException("Error getting attended assists: " + e.getMessage(), e);
        }

        return new PaginatedAttendedAssistResponseDto(attendedAssists, total, limit, skip);
    }

    @Override
    public Optional<PendingAssistDetailResponseDto> getPendingAssistById(UUID assistId) {
        String sql = "SELECT "
                + "a.id, "
                + "r.full_name, "
                + "COALESCE(r.image_data, 'output-files/resident-image/default.png') AS image_data, "
                + "DATE_PART('year', AGE(r.birth_date)) as age, "
                + "r.residential_unit, "
                + "NOW() - a.called_at AS elapsed_time, "
                + "a.severity_level, "
                + "CASE WHEN a.assignment_at IS NULL THEN 'pending' ELSE 'in_attendance' END AS status "
                + "FROM assist a "
                + "JOIN resident r ON a.resident_id = r.id "
                + "WHERE a.id = ? AND a.end_at IS NULL";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, assistId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String imagePath = resultSet.getString("image_data");
                    String base64Image = encodeFileToBase64(imagePath);
                    return Optional.of(new PendingAssistDetailResponseDto(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("full_name"),
                            resultSet.getInt("age"),
                            resultSet.getString("residential_unit"),
                            resultSet.getString("elapsed_time"),
                            SeverityLevel.fromValue(resultSet.getString("severity_level")),
                            resultSet.getString("status"),
                            base64Image
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting pending assist by id: {0}", e.getMessage());
            throw new RuntimeException("Error getting pending assist by id: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    public Long getTotalActiveAlerts() {
        String sql = "SELECT COUNT(*) FROM assist WHERE end_at IS NULL";
        try (var preparedStatement = connection.prepareStatement(sql); var resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return 0L;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error counting active alerts: {0}", e.getMessage());
            throw new RuntimeException("Error counting active alerts", e);
        }
    }

    public Long getTotalSolvedToday() {
        String sql = "SELECT COUNT(*) FROM assist WHERE end_at >= CURRENT_DATE";
        try (var preparedStatement = connection.prepareStatement(sql); var resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return 0L;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error counting solved assists today: {0}", e.getMessage());
            throw new RuntimeException("Error counting solved assists today", e);
        }
    }

    public String getAssistMeanTime() {
        String sql = "SELECT AVG(end_at - called_at) FROM assist WHERE end_at IS NOT NULL";
        try (var preparedStatement = connection.prepareStatement(sql); var resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                // "02:41:15" 2h 41m 15s
                String interval = resultSet.getString(1);
                if (interval != null) {
                    return interval.split("\\.")[0];
                }
                return "0 segundos";
            }
            return "0 segundos";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error calculating mean assist time: {0}", e.getMessage());
            throw new RuntimeException("Error calculating mean assist time", e);
        }
    }

    @Override
    public Optional<AssistHistoryResponseDto> getAssistHistoryById(UUID assistId) {
        String sql = "SELECT "
                + "a.id, "
                + "r.full_name, "
                + "COALESCE(r.image_data, 'output-files/resident-image/default.png') AS image_data, "
                + "DATE_PART('year', AGE(r.birth_date)) as age, "
                + "r.residential_unit, "
                + "a.end_at - a.called_at AS elapsed_time, "
                + "a.severity_level, "
                + "'finalizado' as status, "
                + "a.detail "
                + "FROM assist a "
                + "JOIN resident r ON a.resident_id = r.id "
                + "WHERE a.id = ? AND a.end_at IS NOT NULL";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, assistId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String imagePath = resultSet.getString("image_data");
                    String base64Image = encodeFileToBase64(imagePath);
                    return Optional.of(new AssistHistoryResponseDto(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("full_name"),
                            resultSet.getInt("age"),
                            resultSet.getString("residential_unit"),
                            resultSet.getString("elapsed_time"),
                            SeverityLevel.fromValue(resultSet.getString("severity_level")),
                            resultSet.getString("status"),
                            resultSet.getString("detail"),
                            base64Image
                    ));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting assist history by id: {0}", e.getMessage());
            throw new RuntimeException("Error getting assist history by id: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Assist> findById(UUID assistId) {
        String sql = "SELECT * FROM assist WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, assistId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Assist.mapFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding Assist by id: {0}", e.getMessage());
        }
        return Optional.empty();
    }
}
