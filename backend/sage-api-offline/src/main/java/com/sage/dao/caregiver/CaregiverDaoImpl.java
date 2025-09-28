package com.sage.dao.caregiver;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.port.dao.caregiver.CaregiverDao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CaregiverDaoImpl implements CaregiverDao {

    private final Connection connection;

    public CaregiverDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID createCaregiver(String fullName, String cpf, String email, String phone, String token, UUID organizationId, String position) {
        String sql = "INSERT INTO caregiver (id, organization_id, full_name, active, phone, email, cpf, token, last_used_token, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?, null, ?)";
        UUID caregiverId = UUID.randomUUID();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, caregiverId);
            ps.setObject(2, organizationId);
            ps.setString(3, fullName);
            ps.setBoolean(4, true);
            ps.setString(5, phone);
            ps.setString(6, email);
            ps.setString(7, cpf);
            ps.setString(8, token);
            ps.setString(9, position);
            ps.executeUpdate();
            return caregiverId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CaregiverResponseDto> getAllCaregivers(int limit, int skip, String search) {
        StringBuilder sql = new StringBuilder("SELECT id, full_name, cpf, token, active, last_used_token FROM caregiver");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" WHERE full_name ILIKE ? OR email ILIKE ? OR phone ILIKE ? OR cpf ILIKE ?");
            String searchPattern = "%" + search + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append(" ORDER BY full_name LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(skip);

        List<CaregiverResponseDto> caregivers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int i = 1;
            for (Object param : params) {
                ps.setObject(i++, param);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caregivers.add(new CaregiverResponseDto(
                        (UUID) rs.getObject("id"),
                        rs.getString("full_name"),
                        rs.getString("cpf"),
                        rs.getString("token"),
                        rs.getBoolean("active"),
                        rs.getObject("last_used_token", OffsetDateTime.class)
                ));
            }
            return caregivers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID getFirstOrganizationId() {
        String sql = "SELECT id FROM organization ORDER BY full_name LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return (UUID) rs.getObject("id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isTokenInUse(String token) {
        String sql = "SELECT 1 FROM caregiver WHERE token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCaregiver(UUID id, CreateCaregiverRequestDto request) {
        String sql = "UPDATE caregiver SET full_name = ?, cpf = ?, email = ?, phone = ?, position = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, request.fullName());
            ps.setString(2, request.cpf());
            ps.setString(3, request.email());
            ps.setString(4, request.phone());
            ps.setString(5, request.position());
            ps.setObject(6, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCaregiverActiveStatus(UUID id, boolean active) {
        String sql = "UPDATE caregiver SET active = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setObject(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UUID> findByCpf(String cpf) {
        String sql = "SELECT id FROM caregiver WHERE cpf = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of((UUID) rs.getObject("id"));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UUID> findByEmail(String email) {
        String sql = "SELECT id FROM caregiver WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of((UUID) rs.getObject("id"));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UUID> findByPhone(String phone) {
        String sql = "SELECT id FROM caregiver WHERE phone = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of((UUID) rs.getObject("id"));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CaregiverResponseDto> findByToken(String token) {
        String sql = "SELECT id, full_name, cpf, token, active, last_used_token FROM caregiver WHERE token = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new CaregiverResponseDto(
                        (UUID) rs.getObject("id"),
                        rs.getString("full_name"),
                        rs.getString("cpf"),
                        rs.getString("token"),
                        rs.getBoolean("active"),
                        rs.getObject("last_used_token", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CaregiverResponseDto> findById(UUID id) {
        String sql = "SELECT id, full_name, cpf, token, active, last_used_token FROM caregiver WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new CaregiverResponseDto(
                        (UUID) rs.getObject("id"),
                        rs.getString("full_name"),
                        rs.getString("cpf"),
                        rs.getString("token"),
                        rs.getBoolean("active"),
                        rs.getObject("last_used_token", OffsetDateTime.class)
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}