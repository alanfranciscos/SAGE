package com.sage.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sage.model.CaregiverAssignmentResident;

public interface CaregiverAssignmentResidentRepository extends JpaRepository<CaregiverAssignmentResident, UUID> {

    Optional<CaregiverAssignmentResident> findByResidentIdAndEndAtIsNull(UUID residentId);
}
