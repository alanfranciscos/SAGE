package com.sage.port.dao.assist;

import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.dto.v1.assist.response.PendingAssistDetailResponseDto;
import com.sage.model.assist.Assist;

import java.util.Optional;
import java.util.UUID;

public interface AssistDao {

    /**
     * Creates a new assist record in the database.
     *
     * @param assist the Assist object to create
     * @return the UUID of the created Assist record
     */
    UUID create(Assist assist);

    /**
     * Updates an existing assist record in the database.
     *
     * @param assist the Assist object to update
     * @return the UUID of the updated Assist record
     */
    UUID update(Assist assist);

    /**
     * Finds the current caregiver assignment for a resident. This method
     * retrieves the caregiver assignment for a resident that is currently
     * active (i.e., has no end time).
     *
     * @param residentId the UUID of the resident
     * @return an Optional containing the CaregiverAssignmentResident if found,
     * or empty if not found
     */
    Optional<Assist> findByResidentIdAndEndAtIsNull(UUID residentId);

    PaginatedPendingAssistResponseDto getPendingAssists(int limit, int skip);

    PaginatedAttendedAssistResponseDto getAttendedAssists(int limit, int skip);

    Optional<PendingAssistDetailResponseDto> getPendingAssistById(UUID assistId);

}
