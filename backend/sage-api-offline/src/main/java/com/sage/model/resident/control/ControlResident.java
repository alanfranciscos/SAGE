package com.sage.model.resident.control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;

import lombok.Data;

@Data
public class ControlResident {

    private static final UUID alarmIdDefault = UUID.fromString("a1b2c3d4-e5f6-4789-a012-3456789abcde");

    private UUID id;
    private Integer controlId;
    private UUID alarmId;
    private UUID residentId;

    /**
     * Gets the default alarm ID.
     *
     * @return The default alarm ID as a UUID.
     */
    public static UUID getAlarmIdDefault() {
        return alarmIdDefault;
    }

    /**
     * Maps the fields of this ControlResident object from a
     * CreateResidentRequestDto.
     *
     * @param requestDto The CreateResidentRequestDto containing control data.
     * @param residentId The UUID of the resident to whom this control belongs.
     * @return The current ControlResident object with fields populated from the
     * CreateResidentRequestDto.
     */
    public static ControlResident mapFromCreateResidentRequestDto(
            CreateResidentRequestDto requestDto, UUID residentId
    ) {
        ControlResident controlResident = new ControlResident();
        controlResident.setControlId(requestDto.controlNumber());
        controlResident.setAlarmId(alarmIdDefault);
        controlResident.setResidentId(residentId);
        return controlResident;
    }

    /**
     * Maps the fields of this ControlResident object from a ResultSet.
     *
     * @param resultSet The ResultSet containing control data.
     * @return The current ControlResident object with fields populated from the
     * ResultSet.
     */
    public static ControlResident mapFromResultSet(ResultSet resultSet) {
        try {
            ControlResident controlResident = new ControlResident();
            controlResident.setId(UUID.fromString(resultSet.getString("id")));
            controlResident.setControlId(resultSet.getInt("control_id"));
            controlResident.setAlarmId(UUID.fromString(resultSet.getString("alarm_id")));
            controlResident.setResidentId(UUID.fromString(resultSet.getString("resident_id")));
            return controlResident;
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ControlResident from ResultSet: " + e.getMessage(), e);
        }
    }
}
