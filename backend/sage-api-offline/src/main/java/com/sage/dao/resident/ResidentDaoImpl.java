package com.sage.dao.resident;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.resident.Resident;
import com.sage.port.dao.resident.ResidentDao;

public class ResidentDaoImpl implements ResidentDao {

    private static final Logger logger = Logger.getLogger(ResidentDaoImpl.class.getName());

    private final Connection connection;

    public ResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID createResident(Resident resident) {
        String sql = "INSERT INTO resident (id, full_name, cpf, sex, birth_date, created_at, updated_at, residential_unit, active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            UUID residentId = UUID.randomUUID();
            preparedStatement.setObject(1, residentId);
            preparedStatement.setString(2, resident.getFullName());
            preparedStatement.setString(3, resident.getCpf());
            preparedStatement.setString(4, Character.toString(resident.getSex()));
            preparedStatement.setTimestamp(5, Timestamp.from(resident.getBirthDate().toInstant()));
            preparedStatement.setTimestamp(6, Timestamp.from(resident.getCreatedAt().toInstant()));
            preparedStatement.setTimestamp(7, Timestamp.from(resident.getUpdatedAt().toInstant()));
            preparedStatement.setString(8, resident.getResidentialUnit());
            preparedStatement.setBoolean(9, resident.isActive());
            preparedStatement.executeUpdate();
            return residentId;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating resident: {0}", e.getMessage());
            throw new RuntimeException("Error creating resident", e);
        }
    }

    @Override
    public void updateImageData(UUID residentId, String imageData) {
        String sql = "UPDATE resident SET image_data = ? WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, imageData);
            preparedStatement.setObject(2, residentId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating image data for resident {0}: {1}", new Object[]{residentId, e.getMessage()});
            throw new RuntimeException("Error updating image data for resident", e);
        }
    }

    @Override
    public Resident updateResident(Resident resident) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateResident'");
    }

    @Override
    public Resident findResidentById(UUID residentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findResidentById'");
    }

    @Override
    public Long countResidents() {
        String sql = "SELECT COUNT(*) FROM resident";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error counting residents: {0}", e.getMessage());
        }
        return null;
    }

}
