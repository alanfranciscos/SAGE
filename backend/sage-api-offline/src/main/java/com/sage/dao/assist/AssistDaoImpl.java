package com.sage.dao.assist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.assist.Assist;
import com.sage.port.dao.assist.AssistDao;

public class AssistDaoImpl implements AssistDao {

    private static final Logger logger = Logger.getLogger(AssistDaoImpl.class.getName());

    private final Connection connection;

    public AssistDaoImpl(Connection connection) {
        this.connection = connection;
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
}
