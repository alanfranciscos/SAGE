package com.sage.model.resident;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;

import lombok.Data;

/**
 * Represents a resident in the system. This class contains all the necessary
 * fields to describe a resident, including personal information, residential
 * unit, and status.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@Data
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
    public Resident mapFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = UUID.fromString(resultSet.getString("id"));
        this.fullName = resultSet.getString("full_name");
        this.cpf = resultSet.getString("cpf");
        this.sex = resultSet.getString("sex").charAt(0);
        this.birthDate = resultSet.getTimestamp("birth_date").toInstant().atZone(ZonedDateTime.now().getZone());
        this.createdAt = resultSet.getTimestamp("created_at").toInstant().atZone(ZonedDateTime.now().getZone());
        this.updatedAt = resultSet.getTimestamp("updated_at").toInstant().atZone(ZonedDateTime.now().getZone());
        this.residentialUnit = resultSet.getString("residential_unit");
        this.imageData = resultSet.getString("image_data");
        this.active = resultSet.getBoolean("active");
        return this;
    }
}
