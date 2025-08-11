package com.sage.dao.resident;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.ResidentHeader;
import com.sage.port.dao.resident.ResidentHeaderDao;

public class ResidentHeaderDaoImpl implements ResidentHeaderDao {

    private static final Logger logger = Logger.getLogger(ResidentHeaderDaoImpl.class.getName());

    private final Connection connection;

    public ResidentHeaderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ResidentHeader> listResidentsBySeverityLevelAssist(
            SeverityLevel severityLevel, int limit, int skip, String search
    ) {
        String sql = "";
        if (severityLevel != null) {
            sql += "SELECT r.id, r.full_name, r.residential_unit, r.image_data FROM resident r "
                    + "INNER JOIN assist a ON a.resident_id = r.id "
                    + "WHERE a.severity_level = ? AND a.end_at IS NULL "
                    + "ORDER BY a.called_at DESC LIMIT ? OFFSET ?";
        } else {
            sql += "SELECT DISTINCT r.id, r.full_name, r.residential_unit, r.image_data FROM resident r "
                    + "LEFT JOIN assist a ON a.resident_id = r.id WHERE "
                    + "a.resident_id IS NULL OR a.end_at IS NOT NULL ";
            if (search != null && !search.trim().isEmpty()) {
                sql += " AND (r.full_name LIKE ? OR r.cpf LIKE ?)";
            }
            sql += "ORDER BY r.full_name ASC LIMIT ? OFFSET ?";
        }

        try (var preparedStatement = connection.prepareStatement(sql)) {
            if (severityLevel != null) {
                preparedStatement.setString(1, severityLevel.getValue());
                preparedStatement.setInt(2, limit);
                preparedStatement.setInt(3, skip);
            } else {
                if (search != null && !search.trim().isEmpty()) {
                    preparedStatement.setString(1, "%" + search + "%");
                    preparedStatement.setString(2, "%" + search + "%");
                    preparedStatement.setInt(3, limit);
                    preparedStatement.setInt(4, skip);
                } else {
                    preparedStatement.setInt(1, limit);
                    preparedStatement.setInt(2, skip);
                }

            }

            try (var resultSet = preparedStatement.executeQuery()) {
                List<ResidentHeader> residents = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    residents.add(ResidentHeader.mapFromResultSet(resultSet));
                }
                return residents;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing residents by severity level: {0}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<ResidentHeader> searchResident(String search) {
        String sql = "SELECT r.id, r.full_name, r.residential_unit, r.image_data FROM resident r "
                + "WHERE r.full_name LIKE ? OR r.cpf LIKE ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + search + "%");
            preparedStatement.setString(2, "%" + search + "%");
            try (var resultSet = preparedStatement.executeQuery()) {
                List<ResidentHeader> residents = new java.util.ArrayList<>();
                while (resultSet.next()) {
                    residents.add(ResidentHeader.mapFromResultSet(resultSet));
                }
                return residents;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error searching residents: {0}", e.getMessage());
        }
        return null;
    }
}
