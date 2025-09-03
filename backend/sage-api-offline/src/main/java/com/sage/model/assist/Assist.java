package com.sage.model.assist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Assist {

    private UUID id;
    private UUID caregiverId;
    private UUID residentId;
    private ZonedDateTime calledAt;
    private ZonedDateTime assignmentAt;
    private ZonedDateTime endAt;
    private String detail;
    private SeverityLevel severityLevel;

    public Assist(
            UUID residentId,
            ZonedDateTime calledAt,
            SeverityLevel severityLevel
    ) {
        this.residentId = residentId;
        this.calledAt = calledAt;
        this.severityLevel = severityLevel;
    }

    public static Assist mapFromResultSet(ResultSet resultSet) {
        try {

            Assist assist = new Assist();
            assist.setId(UUID.fromString(resultSet.getString("id")));
            assist.setCaregiverId(
                    resultSet.getString("caregiver_id") == null ? null : UUID.fromString(resultSet.getString("caregiver_id"))
            );
            assist.setResidentId(UUID.fromString(resultSet.getString("resident_id")));
            assist.setCalledAt(resultSet.getTimestamp("called_at").toInstant().atZone(ZoneId.systemDefault()));
            assist.setAssignmentAt(resultSet.getTimestamp("assignment_at") != null ? resultSet.getTimestamp("assignment_at").toInstant().atZone(ZoneId.systemDefault()) : null);
            assist.setEndAt(resultSet.getTimestamp("end_at") != null ? resultSet.getTimestamp("end_at").toInstant().atZone(ZoneId.systemDefault()) : null);
            assist.setDetail(resultSet.getString("detail"));
            assist.setSeverityLevel(SeverityLevel.fromValue(resultSet.getString("severity_level")
            ));
            return assist;
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to Assist", e);
        }
    }

}
