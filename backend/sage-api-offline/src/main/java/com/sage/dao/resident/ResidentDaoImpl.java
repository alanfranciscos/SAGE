package com.sage.dao.resident;

import java.sql.Connection;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createResident'");
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
