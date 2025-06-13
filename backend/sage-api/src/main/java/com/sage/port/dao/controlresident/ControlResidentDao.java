package com.sage.port.dao.controlresident;

import java.util.Optional;

import com.sage.model.ControlResident;
import com.sage.port.dao.crud.CrudDao;

public interface ControlResidentDao extends CrudDao<ControlResident, Long> {

    /**
     * Finds a ControlResident by its controlId.
     *
     * @param controlId the control ID to search for
     * @return an Optional containing the ControlResident if found, or empty if
     * not found
     */
    Optional<ControlResident> findByControlId(String controlId);

}
