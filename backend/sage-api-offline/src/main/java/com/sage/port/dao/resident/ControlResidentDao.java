package com.sage.port.dao.resident;

import java.util.UUID;

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
    ControlResident getByClientId(UUID residentId);

}
