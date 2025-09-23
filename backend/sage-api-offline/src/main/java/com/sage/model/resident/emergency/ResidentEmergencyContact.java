package com.sage.model.resident.emergency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;

import lombok.Data;

@Data
public class ResidentEmergencyContact {

    private UUID id;
    private UUID residentId;
    private String fullName;
    private String phone;
    private String relationship;

    public boolean hasAnyFields() {
        return (fullName != null || phone != null || relationship != null) && residentId != null;
    }

    /**
     * Maps the fields of this ResidentEmergencyContact object from a
     * CreateResidentRequestDto.
     *
     * @param requestDto The CreateResidentRequestDto containing emergency
     * contact data.
     * @param residentId The UUID of the resident to whom this contact belongs.
     * @return The current ResidentEmergencyContact object with fields populated
     * from the CreateResidentRequestDto.
     */
    public static ResidentEmergencyContact mapFromCreateResidentRequestDto(
            CreateResidentRequestDto requestDto,
            UUID residentId
    ) {
        ResidentEmergencyContact contact = new ResidentEmergencyContact();
        contact.setResidentId(residentId);
        contact.setFullName(requestDto.emergencyName());
        contact.setPhone(requestDto.emergencyPhone());
        contact.setRelationship(requestDto.relationship());
        return contact;
    }

    public static ResidentEmergencyContact mapFromResultSet(ResultSet resultSet) {
        try {
            ResidentEmergencyContact contact = new ResidentEmergencyContact();
            contact.setId(UUID.fromString(resultSet.getString("id")));
            contact.setResidentId(UUID.fromString(resultSet.getString("resident_id")));
            contact.setFullName(resultSet.getString("full_name"));
            contact.setPhone(resultSet.getString("phone"));
            contact.setRelationship(resultSet.getString("relationship"));
            return contact;
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResidentEmergencyContact from ResultSet: " + e.getMessage(), e);
        }
    }

}
