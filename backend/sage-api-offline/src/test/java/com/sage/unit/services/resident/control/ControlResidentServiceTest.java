package com.sage.unit.services.resident.control;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.model.resident.control.ControlResident;
import com.sage.port.dao.resident.ControlResidentDao;
import com.sage.services.resident.control.ControlResidentServiceImpl;

@ExtendWith(MockitoExtension.class)
class ControlResidentServiceTest {

    @Mock
    private ControlResidentDao controlResidentDao;

    @InjectMocks
    private ControlResidentServiceImpl service;

    @Test
    void create_ShouldReturnNull_WhenControlIdNull() {
        CreateResidentRequestDto request = new CreateResidentRequestDto(
                "Name", "123", 'M', ZonedDateTime.now(),
                null, null, null, "Unit", null
        );

        UUID result = service.create(request, UUID.randomUUID());

        assertThat(result).isNull();
    }

    @Test
    void create_ShouldReturnId_WhenControlIdPresent() {
        CreateResidentRequestDto request = new CreateResidentRequestDto(
                "Name", "123", 'M', ZonedDateTime.now(),
                null, null, null, "Unit", 123
        );
        UUID expectedId = UUID.randomUUID();
        when(controlResidentDao.create(any(ControlResident.class))).thenReturn(expectedId);

        UUID result = service.create(request, UUID.randomUUID());

        assertThat(result).isEqualTo(expectedId);
        verify(controlResidentDao).create(any(ControlResident.class));
    }

    @Test
    void existsResidentByControlIdAndAlarmId_ShouldReturnDaoResult() {
        when(controlResidentDao.existsResidentByControlIdAndAlarmId(1, "alarm")).thenReturn(true);
        assertThat(service.existsResidentByControlIdAndAlarmId(1, "alarm")).isTrue();
    }
}
