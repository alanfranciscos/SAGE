package com.sage.services.controlresident;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.model.ControlResident;
import com.sage.port.services.controlresident.ControlResidentService;
import com.sage.repository.ControlResidentRepository;

@Service
public class ControlResidentServiceImpl implements ControlResidentService {

    private static final Logger logger = Logger.getLogger(ControlResidentServiceImpl.class.getName());

    private final ControlResidentRepository controlResidentRepository;

    public ControlResidentServiceImpl(com.sage.repository.ControlResidentRepository controlResidentRepository) {
        this.controlResidentRepository = controlResidentRepository;
    }

    @Override
    public Long createControlResident(String controlId, UUID residentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createControlResident'");
    }

    @Override
    public ControlResident updateControlResident(Long id, String controlId, UUID residentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateControlResident'");
    }

    @Override
    public void deleteControlResidentById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteControlResidentById'");
    }

    @Override
    public ControlResident getControlResidentById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getControlResidentById'");
    }

    @Override
    public ControlResident getControlResidentByControlId(String controlId) {

        ControlResident controlResident = controlResidentRepository.findByControlId(controlId).orElseThrow(() -> {
            logger.log(Level.INFO, "Control resident not found for control ID: {0}", controlId);
            throw new IllegalArgumentException("Control resident not found");
        });
        return controlResident;
    }
}
