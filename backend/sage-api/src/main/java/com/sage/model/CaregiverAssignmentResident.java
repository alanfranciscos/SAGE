package com.sage.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

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
}
