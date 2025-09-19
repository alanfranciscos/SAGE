package com.sage.dao.assist;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

@Repository
public class AssistDaoImpl {

    private static final Logger logger = Logger.getLogger(AssistDaoImpl.class.getName());

    private final Connection connection;

    public AssistDaoImpl(Connection connection) {
        this.connection = connection;
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
                    return interval;
                }
                return "0 segundos";
            }
            return "0 segundos";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error calculating mean assist time: {0}", e.getMessage());
            throw new RuntimeException("Error calculating mean assist time", e);
        }
    }
}
