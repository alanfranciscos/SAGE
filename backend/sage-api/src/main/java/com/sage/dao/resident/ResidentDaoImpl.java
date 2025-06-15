package com.sage.dao.resident;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sage.model.Resident;
import com.sage.port.dao.resident.ResidentDao;

public class ResidentDaoImpl implements ResidentDao {

    private static final Logger logger = Logger.getLogger(ResidentDaoImpl.class.getName());

    private final Connection connection;

    public ResidentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    private Resident mapRowToResident(ResultSet resultSet) throws SQLException {
        Resident resident = new Resident();

        resident.setId(resultSet.getObject("id", UUID.class));
        resident.setFullName(resultSet.getString("full_name"));
        resident.setCpf(resultSet.getString("cpf"));

        String sexStr = resultSet.getString("sex");
        Character sexChar = (sexStr != null && !sexStr.isEmpty()) ? sexStr.charAt(0) : null;
        resident.setSex(sexChar);

        resident.setBirthDate(resultSet.getTimestamp("birth_date") != null
                ? resultSet.getTimestamp("birth_date")
                        .toInstant().atZone(java.time.ZoneId.systemDefault()) : null);

        resident.setCreatedAt(resultSet.getTimestamp("created_at") != null
                ? resultSet.getTimestamp("created_at")
                        .toInstant().atZone(java.time.ZoneId.systemDefault()) : null);

        resident.setUpdatedAt(resultSet.getTimestamp("updated_at") != null
                ? resultSet.getTimestamp("updated_at")
                        .toInstant().atZone(java.time.ZoneId.systemDefault()) : null);

        resident.setResidentialUnit(resultSet.getString("residential_unit"));
        resident.setImageData(resultSet.getString("image_data"));
        return resident;
    }

    @Override
    public Resident save(Resident entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<Resident> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public Resident updateInformation(UUID id, Resident entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInformation'");
    }

    @Override
    public void deleteById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Optional<Resident> findById(UUID id) {
        if (id == null) {
            logger.log(Level.WARNING, "Resident ID is null");
            return Optional.empty();
        }

        String sql = "SELECT * FROM resident WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToResident(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding Resident by id: {0}", e.getMessage());
        }
        return Optional.empty();
    }

}
