package com.sage.port.services.resident;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.google.api.client.util.DateTime;
import com.sage.model.Resident;

public interface ResidentService {

    UUID createResident(
            String fullName,
            String cpf,
            char sex,
            DateTime birthDate,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            String residentialUnit,
            String imageData
    );

    Resident updateResident(
            UUID id,
            String fullName,
            char sex,
            DateTime birthDate,
            ZonedDateTime updatedAt,
            String residentialUnit,
            String imageData
    );

    void deleteResidentById(
            UUID id
    );

    Resident getResidentById(
            UUID id
    );
}
