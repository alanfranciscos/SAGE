package com.sage.dao.assist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.CaregiverAssignmentResident;
import com.sage.port.dao.assist.CaregiverAssignmentResidentDao;

public class CaregiverAssignmentResidentDaoImpl implements CaregiverAssignmentResidentDao {

    private static final Logger logger = Logger.getLogger(CaregiverAssignmentResidentDaoImpl.class.getName());

    private final Connection connection;

    public CaregiverAssignmentResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private PreparedStatement prepareParametersToSave(
            PreparedStatement preparedStatement,
            CaregiverAssignmentResident entity
    ) throws SQLException {

        preparedStatement.setObject(1, entity.getCaregiverId());
        preparedStatement.setObject(2, entity.getResidentId());

        preparedStatement.setObject(3, entity.getCalledAt() != null ? entity.getCalledAt().toOffsetDateTime() : null);
        preparedStatement.setObject(4, entity.getAssignmentAt() != null ? entity.getAssignmentAt().toOffsetDateTime() : null);
        preparedStatement.setObject(5, entity.getEndAt() != null ? entity.getEndAt().toOffsetDateTime() : null);

        preparedStatement.setString(6, entity.getDetail());
        preparedStatement.setInt(7, entity.getSeverityLevel());

        return preparedStatement;
    }

    private CaregiverAssignmentResident mapResultToEntity(ResultSet resultSet) throws SQLException {
        CaregiverAssignmentResident entity = new CaregiverAssignmentResident();

        entity.setId(resultSet.getObject("id", UUID.class));
        entity.setCaregiverId(resultSet.getObject("caregiver_id", UUID.class));
        entity.setResidentId(resultSet.getObject("resident_id", UUID.class));
        entity.setCalledAt(resultSet.getTimestamp("called_at").toInstant().atZone(java.time.ZoneId.systemDefault()));
        entity.setAssignmentAt(resultSet.getTimestamp("assignment_at") != null
                ? resultSet.getTimestamp("assignment_at").toInstant().atZone(java.time.ZoneId.systemDefault()) : null
        );
        entity.setEndAt(
                resultSet.getTimestamp("end_at") != null
                ? resultSet.getTimestamp("end_at")
                        .toInstant().atZone(java.time.ZoneId.systemDefault()) : null
        );
        entity.setDetail(resultSet.getString("detail"));
        entity.setSeverityLevel(resultSet.getInt("severity_level"));

        return entity;
    }

    @Override
    public CaregiverAssignmentResident save(CaregiverAssignmentResident entity) {
        String sql = "INSERT INTO caregiver_assignment_resident";
        sql += " (caregiver_id, resident_id, called_at, assignment_at, end_at, detail, severity_level)";
        sql += " VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection.setAutoCommit(false); // Start transaction
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement = this.prepareParametersToSave(preparedStatement, entity);
            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                final UUID id = (UUID) resultSet.getObject(1);
                entity.setId(id);
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
                    "Error saving CaregiverAssignmentResident: {0}", e.getMessage()
            );
            throw new RuntimeException("Error saving CaregiverAssignmentResident: " + e.getMessage(), e);
        }

        return entity;
    }

    @Override
    public List<CaregiverAssignmentResident> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public CaregiverAssignmentResident updateInformation(UUID id, CaregiverAssignmentResident entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInformation'");
    }

    @Override
    public void deleteById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Optional<CaregiverAssignmentResident> findByResidentIdAndEndAtIsNull(UUID residentId) {
        String sql = "SELECT * FROM caregiver_assignment_resident WHERE resident_id = ? AND end_at IS NULL";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, residentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding CaregiverAssignmentResident: {0}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<CaregiverAssignmentResident> findById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
