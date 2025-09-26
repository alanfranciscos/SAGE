package com.sage.port.services.assist;

import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;

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

    PaginatedPendingAssistResponseDto getPendingAssists(int limit, int skip);

    PaginatedAttendedAssistResponseDto getAttendedAssists(int limit, int skip);
}
