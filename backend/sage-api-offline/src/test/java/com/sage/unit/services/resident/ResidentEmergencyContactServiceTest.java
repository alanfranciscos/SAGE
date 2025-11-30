package com.sage.unit.services.resident;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.model.resident.emergency.ResidentEmergencyContact;
import com.sage.port.dao.resident.ResidentEmergencyContactDao;
import com.sage.services.resident.ResidentEmergencyContactServiceImpl;

@ExtendWith(MockitoExtension.class)
class ResidentEmergencyContactServiceTest {

    @Mock
    private ResidentEmergencyContactDao residentEmergencyContactDao;

    @InjectMocks
    private ResidentEmergencyContactServiceImpl service;

    @Test
    void create_ShouldReturnNull_WhenNoFields() {
        CreateResidentRequestDto request = new CreateResidentRequestDto(
                "Name", "123", 'M', ZonedDateTime.now(),
                null, null, null,
                "Unit", 123
        );

        UUID result = service.create(request, UUID.randomUUID());

        assertThat(result).isNull();
    }

    @Test
    void create_ShouldReturnId_WhenFieldsPresent() {
        CreateResidentRequestDto request = new CreateResidentRequestDto(
                "Name", "123", 'M', ZonedDateTime.now(),
                "Emerg Name", "Phone", "Rel",
                "Unit", 123
        );
        UUID residentId = UUID.randomUUID();
        UUID expectedId = UUID.randomUUID();

        when(residentEmergencyContactDao.create(any(ResidentEmergencyContact.class))).thenReturn(expectedId);

        UUID result = service.create(request, residentId);

        assertThat(result).isEqualTo(expectedId);
        verify(residentEmergencyContactDao).create(any(ResidentEmergencyContact.class));
    }

    @Test
    void getByClientId_ShouldReturnContact() {
        UUID clientId = UUID.randomUUID();
        ResidentEmergencyContact expected = new ResidentEmergencyContact();
        when(residentEmergencyContactDao.getByClientId(clientId)).thenReturn(expected);

        ResidentEmergencyContact result = service.getByClientId(clientId);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void update_ShouldCallDao() {
        UpdateResidentRequestDto request = new UpdateResidentRequestDto(
                "Name", "123", 'M', ZonedDateTime.now(),
                "Emerg Name", "Phone", "Rel",
                "Unit", 123
        );
        UUID residentId = UUID.randomUUID();

        service.update(request, residentId);

        verify(residentEmergencyContactDao).update(eq(request), eq(residentId));
    }
}
