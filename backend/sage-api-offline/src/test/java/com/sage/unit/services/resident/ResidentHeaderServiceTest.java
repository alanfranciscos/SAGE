package com.sage.unit.services.resident;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.ResidentHeader;
import com.sage.port.dao.resident.ResidentDao;
import com.sage.port.dao.resident.ResidentHeaderDao;
import com.sage.services.resident.ResidentHeaderServiceImpl;

@ExtendWith(MockitoExtension.class)
class ResidentHeaderServiceTest {

    @Mock
    private ResidentHeaderDao residentHeaderDao;

    @Mock
    private ResidentDao residentDao;

    @InjectMocks
    private ResidentHeaderServiceImpl service;

    @Test
    void listResidents_ShouldPrioritizeEmergency() {
        int limit = 10;
        int skip = 0;
        Long total = 100L;

        when(residentDao.countResidents()).thenReturn(total);

        List<ResidentHeader> emergencyList = new ArrayList<>();
        emergencyList.add(new ResidentHeader());

        when(residentHeaderDao.listResidentsBySeverityLevelAssist(SeverityLevel.EMERGENCY, limit, skip, null))
                .thenReturn(emergencyList);

        List<ResidentHeader> warningList = new ArrayList<>();
        when(residentHeaderDao.listResidentsBySeverityLevelAssist(SeverityLevel.WARNING, 9, skip, null))
                .thenReturn(warningList);

        List<ResidentHeader> normalList = new ArrayList<>();
        normalList.add(new ResidentHeader());
        when(residentHeaderDao.listResidentsBySeverityLevelAssist(null, 9, skip, null))
                .thenReturn(normalList);

        ResidentListResponseDto response = service.listResidents(limit, skip, null);

        assertThat(response.severalResidents()).hasSize(1);
        assertThat(response.warningResidents()).isEmpty();
        assertThat(response.normalResidents()).hasSize(1);
        assertThat(response.totalResidents()).isEqualTo(total);
    }

    @Test
    void listResidents_WhenEmergencyFillsLimit_ShouldNotCallOthers() {
        int limit = 2;
        int skip = 0;
        Long total = 100L;

        when(residentDao.countResidents()).thenReturn(total);

        List<ResidentHeader> emergencyList = new ArrayList<>();
        emergencyList.add(new ResidentHeader());
        emergencyList.add(new ResidentHeader());

        when(residentHeaderDao.listResidentsBySeverityLevelAssist(SeverityLevel.EMERGENCY, limit, skip, null))
                .thenReturn(emergencyList);

        ResidentListResponseDto response = service.listResidents(limit, skip, null);

        assertThat(response.severalResidents()).hasSize(2);
        assertThat(response.warningResidents()).isEmpty();
        assertThat(response.normalResidents()).isEmpty();

        verify(residentHeaderDao, times(0)).listResidentsBySeverityLevelAssist(SeverityLevel.WARNING, 0, skip, null);
    }
}
