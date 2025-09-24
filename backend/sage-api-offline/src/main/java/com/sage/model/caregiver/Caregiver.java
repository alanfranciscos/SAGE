package com.sage.model.caregiver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a caregiver in the system. This class contains all the necessary
 * fields to describe a caregiver, including personal information, organization
 * association, and token for authentication.
 *
 * @author SAGE System
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caregiver {

    private UUID id;
    private UUID organizationId;
    private String fullName;
    private boolean active;
    private String phone;
    private String email;
    private String cpf;
    private String token;
    private ZonedDateTime lastUsedToken;

    /**
     * Maps the fields of this Caregiver object from a CreateCaregiverRequestDto.
     *
     * @param requestDto The CreateCaregiverRequestDto containing caregiver data.
     * @param organizationId The organization ID to associate with the caregiver.
     * @param token The generated token for the caregiver.
     * @return The current Caregiver object with fields populated from the
     * CreateCaregiverRequestDto.
     */
    public static Caregiver mapFromCreateCaregiverRequestDto(CreateCaregiverRequestDto requestDto, UUID organizationId, String token) {
        Caregiver caregiver = new Caregiver();
        caregiver.setOrganizationId(organizationId);
        caregiver.setFullName(requestDto.fullName());
        caregiver.setActive(true);
        caregiver.setPhone(requestDto.phone());
        caregiver.setEmail(requestDto.email());
        caregiver.setCpf(requestDto.cpf());
        caregiver.setToken(token);
        caregiver.setLastUsedToken(null);
        return caregiver;
    }

    /**
     * Maps the fields of this Caregiver object from a ResultSet.
     *
     * @param resultSet The ResultSet containing caregiver data.
     * @return The current Caregiver object with fields populated from the
     * ResultSet.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    public static Caregiver mapFromResultSet(ResultSet resultSet) {
        try {
            Caregiver caregiver = new Caregiver();
            caregiver.setId(UUID.fromString(resultSet.getString("id")));
            caregiver.setOrganizationId(UUID.fromString(resultSet.getString("organization_id")));
            caregiver.setFullName(resultSet.getString("full_name"));
            caregiver.setActive(resultSet.getBoolean("active"));
            caregiver.setPhone(resultSet.getString("phone"));
            caregiver.setEmail(resultSet.getString("email"));
            caregiver.setCpf(resultSet.getString("cpf"));
            caregiver.setToken(resultSet.getString("token"));
            
            var lastUsedTokenTimestamp = resultSet.getTimestamp("last_used_token");
            if (lastUsedTokenTimestamp != null) {
                caregiver.setLastUsedToken(lastUsedTokenTimestamp.toInstant().atZone(ZoneId.systemDefault()));
            }
            
            return caregiver;
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping Caregiver from ResultSet", e);
        }
    }
}