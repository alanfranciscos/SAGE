package com.sage.model.resident;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a resident in the system. This class contains all the necessary
 * fields to describe a resident, including personal information, residential
 * unit, and status.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resident {

    private UUID id;
    private String fullName;
    private String cpf;
    private char sex;
    private ZonedDateTime birthDate;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String residentialUnit;
    private String imageData;
    private boolean active;

    /**
     * Maps the fields of this Resident object from a CreateResidentRequestDto.
     *
     * Note: The imageData field is set to null as it is not included in the
     *
     * @param requestDto The CreateResidentRequestDto containing resident data.
     * @return The current Resident object with fields populated from the
     * CreateResidentRequestDto.
     */
    public static Resident mapFromCreateResidentRequestDto(CreateResidentRequestDto requestDto) {
        Resident resident = new Resident();
        resident.setFullName(requestDto.fullName());
        resident.setCpf(requestDto.cpf());
        resident.setSex(requestDto.sex());
        resident.setBirthDate(requestDto.birthDate());
        resident.setCreatedAt(ZonedDateTime.now());
        resident.setUpdatedAt(ZonedDateTime.now());
        resident.setResidentialUnit(requestDto.residentialUnit());
        resident.setImageData(null);
        resident.setActive(true);
        return resident;
    }

    /**
     * Maps the fields of this Resident object from a ResultSet.
     *
     * @param resultSet The ResultSet containing resident data.
     * @return The current Resident object with fields populated from the
     * ResultSet.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    public static Resident mapFromResultSet(ResultSet resultSet) {
        try {
            Resident resident = new Resident();
            resident.setId(UUID.fromString(resultSet.getString("id")));
            resident.setFullName(resultSet.getString("full_name"));
            resident.setCpf(resultSet.getString("cpf"));
            resident.setSex(resultSet.getString("sex").charAt(0));
            resident.setBirthDate(resultSet.getTimestamp("birth_date").toInstant().atZone(ZoneId.systemDefault()));
            resident.setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()));
            resident.setUpdatedAt(resultSet.getTimestamp("updated_at").toInstant().atZone(ZoneId.systemDefault()));
            resident.setResidentialUnit(resultSet.getString("residential_unit"));
            resident.setImageData(resultSet.getString("image_data"));
            resident.setActive(resultSet.getBoolean("active"));
            return resident;
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping Resident from ResultSet", e);
        }
    }

    public static Resident mapFromUpdateResidentRequestDto(UpdateResidentRequestDto requestDto) {
        Resident resident = new Resident();
        resident.setFullName(requestDto.fullName());
        resident.setCpf(requestDto.cpf());
        resident.setSex(requestDto.sex());
        resident.setBirthDate(requestDto.birthDate());
        resident.setResidentialUnit(requestDto.residentialUnit());
        return resident;
    }
}
