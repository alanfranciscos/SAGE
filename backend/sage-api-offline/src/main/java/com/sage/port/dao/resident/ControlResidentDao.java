package com.sage.port.dao.resident;

import java.util.List;
import java.util.UUID;

import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.model.resident.control.ControlResident;

public interface ControlResidentDao {

    /**
     * Creates a new resident control record with the provided details.
     *
     * @param controlResident The ControlResident object containing the details
     * to create.
     * @return The UUID of the newly created resident control record.
     */
    UUID create(ControlResident controlResident);

    /**
     * Checks if a resident exists by their control ID and alarm ID.
     *
     * @param controlId The control ID of the resident to check.
     * @param alarmId The alarm ID of the resident to check.
     * @return True if a resident with the given control ID and alarm ID exists,
     * false otherwise.
     */
    boolean existsResidentByControlIdAndAlarmId(Integer controlId, String alarmId);

    /**
     * Retrieves a resident control record by its client ID.
     *
     * @param residentId The UUID of the resident to retrieve.
     * @return The ControlResident object associated with the given resident ID.
     */
    ControlResident getByResidentId(UUID residentId);

    /**
     * Lists all control residents.
     *
     * @return A list of all ControlResident objects.
     */
    List<ControlResident> listControl();

    /**
     * Finds a resident control record by its control ID and alarm ID.
     *
     * @param controlId The control ID of the resident to find.
     * @param alarmId The alarm ID of the resident to find.
     * @return The ControlResident object associated with the given control ID
     * and alarm ID, or null if not found.
     */
    ControlResident findByControlByIdAndAlarmId(Integer controlId, UUID alarmId);

    /**
     * Updates an existing resident control record with the provided details.
     *
     * @param requestDto The UpdateResidentRequestDto object containing the
     * updated details.
     */
    void update(UpdateResidentRequestDto requestDto, UUID id);

}
