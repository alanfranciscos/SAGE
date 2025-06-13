package com.sage.port.dao.assist;

import java.util.Optional;
import java.util.UUID;

import com.sage.model.CaregiverAssignmentResident;
import com.sage.port.dao.crud.CrudDao;

public interface CaregiverAssignmentResidentDao extends CrudDao<CaregiverAssignmentResident, UUID> {

    /**
     * Finds the current caregiver assignment for a resident. This method
     * retrieves the caregiver assignment for a resident that is currently
     * active (i.e., has no end time).
     *
     * @param residentId the UUID of the resident
     * @return an Optional containing the CaregiverAssignmentResident if found,
     * or empty if not found
     */
    Optional<CaregiverAssignmentResident> findByResidentIdAndEndAtIsNull(UUID residentId);

}
