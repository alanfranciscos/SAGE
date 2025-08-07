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

}
