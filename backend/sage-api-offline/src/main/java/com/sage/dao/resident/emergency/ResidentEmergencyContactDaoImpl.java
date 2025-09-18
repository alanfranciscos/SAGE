package com.sage.dao.resident.emergency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.model.resident.emergency.ResidentEmergencyContact;
import com.sage.port.dao.resident.ResidentEmergencyContactDao;

public class ResidentEmergencyContactDaoImpl implements ResidentEmergencyContactDao {

    private static final Logger logger = Logger.getLogger(ResidentEmergencyContactDaoImpl.class.getName());

    private final Connection connection;

    public ResidentEmergencyContactDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID create(ResidentEmergencyContact emergencyContact) {
        String sql = "INSERT INTO resident_emergency_contact (id, resident_id, full_name, phone, relationship) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            UUID residentId = UUID.randomUUID();
            stmt.setObject(1, residentId);
            stmt.setObject(2, emergencyContact.getResidentId());
            stmt.setString(3, emergencyContact.getFullName());
            stmt.setString(4, emergencyContact.getPhone());
            stmt.setString(5, emergencyContact.getRelationship());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return UUID.fromString(generatedKeys.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating resident emergency contact: {0}", e.getMessage());
            throw new RuntimeException("Error creating resident emergency contact", e);
        }
        return null;
    }

    @Override
    public ResidentEmergencyContact getByClientId(UUID clientId) {
        String sql = "SELECT * FROM resident_emergency_contact WHERE resident_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return ResidentEmergencyContact.mapFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving resident emergency contact: {0}", e.getMessage());
            throw new RuntimeException("Error retrieving resident emergency contact", e);
        }
        return null;
    }

    @Override
    public void update(UpdateResidentRequestDto requestDto, UUID residentId) {
        String sql = "UPDATE resident_emergency_contact SET full_name = ?, phone = ?, relationship = ? "
                + "WHERE resident_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, requestDto.emergencyName().orElse(null));
            stmt.setString(2, requestDto.emergencyPhone().orElse(null));
            stmt.setString(3, requestDto.relationship().orElse(null));
            stmt.setObject(4, residentId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating resident emergency contact: {0}", e.getMessage());
            throw new RuntimeException("Error updating resident emergency contact", e);
        }
    }

}
