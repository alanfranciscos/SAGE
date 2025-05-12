package com.sage.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "caregiver_assignment_resident")
@NoArgsConstructor
public class CaregiverAssignmentResident {

    public CaregiverAssignmentResident(
            UUID residentId,
            ZonedDateTime calledAt,
            int severityLevel
    ) {
        this.residentId = residentId;
        this.calledAt = calledAt;
        this.severityLevel = severityLevel;
    }

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(nullable = false, unique = true, updatable = false)
    private UUID id;

    @Column(name = "caregiver_id")
    private UUID caregiverId;

    @Column(nullable = false)
    private UUID residentId;

    @Column(nullable = false)
    private ZonedDateTime calledAt;

    private ZonedDateTime assignmentAt;

    private ZonedDateTime endAt;

    private String detail;

    private int severityLevel;

    public void parseJson(JSONObject jsonObject) {
        this.id = UUID.fromString(jsonObject.getString("id"));

        if (!jsonObject.isNull("caregiver_id")) {
            this.caregiverId = UUID.fromString(jsonObject.getString("caregiver_id"));
        } else {
            this.caregiverId = null;
        }

        this.residentId = UUID.fromString(jsonObject.getString("resident_id"));
        this.calledAt = parseDate(jsonObject, "called_at");
        this.assignmentAt = parseDate(jsonObject, "assignment_at");
        this.endAt = parseDate(jsonObject, "end_at");
        this.detail = jsonObject.optString("detail", null);
        this.severityLevel = jsonObject.optInt("severity_level", 1);
    }

    private ZonedDateTime parseDate(JSONObject jsonObject, String key) {
        if (!jsonObject.isNull(key)) {
            return ZonedDateTime.parse(jsonObject.getString(key));
        }
        return null;
    }

}
