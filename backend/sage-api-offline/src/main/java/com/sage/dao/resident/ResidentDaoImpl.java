package com.sage.dao.resident;

import java.sql.Connection;
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
}
