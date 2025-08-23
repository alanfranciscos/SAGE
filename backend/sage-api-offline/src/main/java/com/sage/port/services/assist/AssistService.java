package com.sage.port.services.assist;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface AssistService {

    UUID createAssist(
            Integer controlId,
            ZonedDateTime calledAt
    );

    void assignCarregiver(
            UUID id,
            UUID carregiverId,
            ZonedDateTime assignmentAt
    );

    void finishAssist(
            UUID id,
            ZonedDateTime endAt
    );
}
