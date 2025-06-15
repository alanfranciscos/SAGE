package com.sage.dao.controlresident;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.ControlResident;
import com.sage.port.dao.controlresident.ControlResidentDao;

public class ControlResidentDaoImpl implements ControlResidentDao {

    private static final Logger logger = Logger.getLogger(ControlResidentDaoImpl.class.getName());

    private final Connection connection;

    public ControlResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private ControlResident mapRowToControlResident(java.sql.ResultSet resultSet) throws java.sql.SQLException {
        ControlResident controlResident = new ControlResident();

        controlResident.setId(resultSet.getLong("id"));
        controlResident.setControlId(resultSet.getString("control_id"));
        controlResident.setResidentId(resultSet.getObject("resident_id", UUID.class));

        return controlResident;
    }

    @Override
    public ControlResident save(ControlResident entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<ControlResident> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public ControlResident updateInformation(Long id, ControlResident entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInformation'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Optional<ControlResident> findByControlId(String controlId) {
        // TODO Auto-generated method stub
        String sql = "SELECT * FROM control_resident WHERE control_id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, controlId);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToControlResident(resultSet));
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding ControlResident by controlId: {0}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<ControlResident> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
