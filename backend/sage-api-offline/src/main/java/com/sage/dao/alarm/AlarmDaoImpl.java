package com.sage.dao.alarm;

import com.sage.model.alarm.Alarm;
import com.sage.port.dao.alarm.AlarmDao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class AlarmDaoImpl implements AlarmDao {

    private static final Logger logger = Logger.getLogger(AlarmDaoImpl.class.getName());
    private final Connection connection;

    public AlarmDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Alarm alarm) {
        String sql = "INSERT INTO alarm (id, model, status, ip_address, mac_address, account, serial_number, port) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, alarm.getId());
            ps.setString(2, alarm.getModel());
            ps.setString(3, alarm.getStatus());
            ps.setString(4, alarm.getIpAddress());
            ps.setString(5, alarm.getMacAddress());
            ps.setString(6, alarm.getAccount());
            ps.setString(7, alarm.getSerialNumber());
            ps.setInt(8, alarm.getPort());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving alarm", e);
            throw new RuntimeException("Error saving alarm", e);
        }
    }

    @Override
    public void update(Alarm alarm) {
        String sql = "UPDATE alarm SET model = ?, status = ?, ip_address = ?, mac_address = ?, account = ?, serial_number = ? " +
                     "WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, alarm.getModel());
            ps.setString(2, alarm.getStatus());
            ps.setString(3, alarm.getIpAddress());
            ps.setString(4, alarm.getMacAddress());
            ps.setString(5, alarm.getAccount());
            ps.setString(6, alarm.getSerialNumber());
            ps.setObject(7, alarm.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating alarm", e);
            throw new RuntimeException("Error updating alarm", e);
        }
    }

    @Override
    public void updatePort(Alarm alarm) {
        String sql = "UPDATE alarm SET port = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, alarm.getPort());
            ps.setObject(2, alarm.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating alarm port", e);
            throw new RuntimeException("Error updating alarm port", e);
        }
    }

    @Override
    public Optional<Alarm> findBySerialNumber(String serialNumber) {
        String sql = "SELECT * FROM alarm WHERE serial_number = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, serialNumber);
            return getAlarmFromResultSet(ps);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding alarm by serial number", e);
            throw new RuntimeException("Error finding alarm by serial number", e);
        }
    }

    @Override
    public Optional<Alarm> findById(UUID id) {
        String sql = "SELECT * FROM alarm WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            return getAlarmFromResultSet(ps);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding alarm by id", e);
            throw new RuntimeException("Error finding alarm by id", e);
        }
    }
    
    private Optional<Alarm> getAlarmFromResultSet(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Alarm alarm = new Alarm();
                alarm.setId((UUID) rs.getObject("id"));
                alarm.setModel(rs.getString("model"));
                alarm.setStatus(rs.getString("status"));
                alarm.setIpAddress(rs.getString("ip_address"));
                alarm.setMacAddress(rs.getString("mac_address"));
                alarm.setAccount(rs.getString("account"));
                alarm.setSerialNumber(rs.getString("serial_number"));
                alarm.setPort(rs.getInt("port"));
                return Optional.of(alarm);
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM alarm WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting alarm", e);
            throw new RuntimeException("Error deleting alarm", e);
        }
    }
}
