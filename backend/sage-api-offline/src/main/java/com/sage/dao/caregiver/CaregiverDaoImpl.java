package com.sage.dao.caregiver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.sage.model.caregiver.Caregiver;
import com.sage.port.dao.caregiver.CaregiverDao;

@Repository
public class CaregiverDaoImpl implements CaregiverDao {

    private static final Logger logger = Logger.getLogger(CaregiverDaoImpl.class.getName());

    private final Connection connection;

    public CaregiverDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UUID createCaregiver(Caregiver caregiver) {
        String sql = """
            INSERT INTO caregiver (organization_id, full_name, active, phone, email, cpf, token, last_used_token)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, caregiver.getOrganizationId());
            stmt.setString(2, caregiver.getFullName());
            stmt.setBoolean(3, caregiver.isActive());
            stmt.setString(4, caregiver.getPhone());
            stmt.setString(5, caregiver.getEmail());
            stmt.setString(6, caregiver.getCpf());
            stmt.setString(7, caregiver.getToken());
            stmt.setObject(8, caregiver.getLastUsedToken());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID caregiverId = UUID.fromString(rs.getString("id"));
                    logger.info("Created caregiver with ID: " + caregiverId);
                    return caregiverId;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error creating caregiver: " + e.getMessage());
            throw new RuntimeException("Error creating caregiver", e);
        }
        
        throw new RuntimeException("Failed to create caregiver - no ID returned");
    }

    @Override
    public UUID getFirstOrganizationId() {
        String sql = "SELECT id FROM organization ORDER BY id LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                UUID organizationId = UUID.fromString(rs.getString("id"));
                logger.info("Found first organization ID: " + organizationId);
                return organizationId;
            }
        } catch (SQLException e) {
            logger.severe("Error getting first organization ID: " + e.getMessage());
            throw new RuntimeException("Error getting first organization ID", e);
        }
        
        throw new RuntimeException("No organizations found in the database");
    }
}
