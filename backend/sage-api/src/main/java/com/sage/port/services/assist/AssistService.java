package com.sage.port.services.assist;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface AssistService {

    UUID createAssist(
            String controlId,
            ZonedDateTime calledAt
    );

    void assignCarregiver(
            Long id,
            String carregiverId,
            ZonedDateTime assignmentAt
    );

    void finishAssist(
            Long id,
            ZonedDateTime endAt
    );
}
