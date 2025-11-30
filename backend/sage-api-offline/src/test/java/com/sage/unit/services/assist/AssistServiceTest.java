package com.sage.unit.services.assist;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sage.dao.assist.AssistDaoImpl;
import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.services.assist.AssistServiceImpl;

@ExtendWith(MockitoExtension.class)
class AssistServiceTest {

    @Mock
    private AssistDaoImpl assistDao;

    @InjectMocks
    private AssistServiceImpl assistService;

    @Test
    void getPendingAssists_ShouldPassSearchParameter() {
        int limit = 10;
        int skip = 0;
        String search = "Maria";
        PaginatedPendingAssistResponseDto expectedResponse = new PaginatedPendingAssistResponseDto(null, 0L, limit, skip);

        when(assistDao.getPendingAssists(limit, skip, search)).thenReturn(expectedResponse);

        assistService.getPendingAssists(limit, skip, search);

        verify(assistDao).getPendingAssists(limit, skip, search);
    }

    @Test
    void getAttendedAssists_ShouldPassSearchParameter() {
        int limit = 10;
        int skip = 0;
        String search = "Joao";
        PaginatedAttendedAssistResponseDto expectedResponse = new PaginatedAttendedAssistResponseDto(null, 0L, limit, skip);

        when(assistDao.getAttendedAssists(limit, skip, search)).thenReturn(expectedResponse);

        assistService.getAttendedAssists(limit, skip, search);

        verify(assistDao).getAttendedAssists(limit, skip, search);
    }
}
