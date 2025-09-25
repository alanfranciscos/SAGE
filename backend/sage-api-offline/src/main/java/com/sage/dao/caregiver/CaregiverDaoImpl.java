package com.sage.dao.caregiver;

import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CaregiverDaoImpl {

    private final Connection connection;

    public CaregiverDaoImpl(Connection connection) {
        this.connection = connection;
    }

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

    public List<CaregiverResponseDto> getAllCaregivers() {
        String sql = "SELECT full_name, cpf, token, active, last_used_token FROM caregiver";
        List<CaregiverResponseDto> caregivers = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caregivers.add(new CaregiverResponseDto(
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
}
