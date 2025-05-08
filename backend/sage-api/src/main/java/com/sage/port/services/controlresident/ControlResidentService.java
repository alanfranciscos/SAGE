package com.sage.port.services.controlresident;

import java.util.UUID;

import com.sage.model.ControlResident;

public interface ControlResidentService {

    Long createControlResident(
            String controlId,
            UUID residentId
    );

    ControlResident updateControlResident(
            Long id,
            String controlId,
            UUID residentId
    );

    void deleteControlResidentById(
            String id
    );

    ControlResident getControlResidentById(
            String id
    );

    ControlResident getControlResidentByControlId(
        String controlId
    );

}
