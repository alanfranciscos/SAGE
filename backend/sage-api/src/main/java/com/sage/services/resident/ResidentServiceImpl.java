package com.sage.services.resident;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.sage.model.Resident;
import com.sage.port.services.resident.ResidentService;
import com.sage.repository.ResidentRepository;

@Service
public class ResidentServiceImpl implements ResidentService {

    private static final Logger logger = Logger.getLogger(ResidentServiceImpl.class.getName());

    private final ResidentRepository residentRepository;

    public ResidentServiceImpl(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    @Override
    public UUID createResident(
            String fullName,
            String cpf,
            char sex,
            DateTime birthDate,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            String residentialUnit,
            String imageData
    ) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Resident updateResident(
            UUID id,
            String fullName,
            char sex,
            DateTime birthDate,
            ZonedDateTime updatedAt,
            String residentialUnit,
            String imageData
    ) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void deleteResidentById(UUID id) {
        // TODO
    }

    @Override
    public Resident getResidentById(UUID id) {

        Resident resident = residentRepository.findById(id).orElseThrow(() -> {
            logger.log(Level.INFO, "Resident not found for ID: {0}", id);
            throw new IllegalArgumentException("Resident not found");
        });

        return resident;
    }
}
