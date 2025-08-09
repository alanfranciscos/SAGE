package com.sage.dao.resident.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.resident.control.ControlResident;
import com.sage.port.dao.resident.ControlResidentDao;

public class ControlResidentDaoImpl implements ControlResidentDao {

    private static final Logger logger = Logger.getLogger(ControlResidentDaoImpl.class.getName());

    private final Connection connection;

    public ControlResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID create(ControlResident controlResident) {
        String sql = "INSERT INTO control_resident (id, control_id, alarm_id, resident_id) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            UUID id = UUID.randomUUID();
            stmt.setObject(1, id);
            stmt.setInt(2, controlResident.getControlId());
            stmt.setObject(3, controlResident.getAlarmId());
            stmt.setObject(4, controlResident.getResidentId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return id;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating control resident: {0}", e.getMessage());
            throw new RuntimeException("Error creating control resident", e);
        }
        return null;
    }

    @Override
    public boolean existsResidentByControlIdAndAlarmId(Integer controlId, String alarmId) {
        String sql = "SELECT COUNT(*) FROM control_resident WHERE control_id = ? AND alarm_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, controlId);
            stmt.setObject(2, UUID.fromString(alarmId));

            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking existence of control resident: {0}", e.getMessage());
            throw new RuntimeException("Error checking existence of control resident", e);
        }
        return false;
    }

    @Override
    public ControlResident getByClientId(UUID residentId) {
        String sql = "SELECT * FROM control_resident WHERE resident_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, residentId);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return ControlResident.mapFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving control resident: {0}", e.getMessage());
            throw new RuntimeException("Error retrieving control resident", e);
        }
        return null;
    }

}
