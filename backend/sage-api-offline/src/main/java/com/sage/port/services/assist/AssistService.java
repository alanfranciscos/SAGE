package com.sage.port.services.assist;

import com.sage.dto.v1.assist.response.AssistHistoryResponseDto;
import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.dto.v1.assist.response.PendingAssistDetailResponseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface AssistService {

    UUID createAssist(
            Integer controlId,
            ZonedDateTime calledAt
    );

    void startAssist(UUID assistId, String caregiverToken);

    void finishAssist(UUID assistId, String caregiverToken, String details);

    PaginatedPendingAssistResponseDto getPendingAssists(int limit, int skip, String search);

    PaginatedAttendedAssistResponseDto getAttendedAssists(int limit, int skip, String search);

    PendingAssistDetailResponseDto getPendingAssistById(UUID assistId);

    AssistHistoryResponseDto getAssistHistoryById(UUID assistId);
}
