package com.sage.unit.services.resident;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sage.dao.assist.AssistDaoImpl;
import com.sage.dao.resident.ResidentDaoImpl;
import com.sage.exception.AlreadyExistsException;
import com.sage.services.resident.ResidentServiceImpl;

@ExtendWith(MockitoExtension.class)
class ResidentServiceTest {

    @Mock
    private ResidentDaoImpl residentDao;

    @Mock
    private AssistDaoImpl assistDao;

    @Mock
    private Connection connection;

    @InjectMocks
    private ResidentServiceImpl residentService;

    @Test
    void getCardTotal_ShouldReturnTotal() {
        when(residentDao.getCardTotal()).thenReturn(50L);
        Long result = residentService.getCardTotal();
        assertThat(result).isEqualTo(50L);
        verify(residentDao).getCardTotal();
    }

    @Test
    void getTotalActiveAlerts_ShouldReturnTotal() {
        when(assistDao.getTotalActiveAlerts()).thenReturn(5L);
        Long result = residentService.getTotalActiveAlerts();
        assertThat(result).isEqualTo(5L);
        verify(assistDao).getTotalActiveAlerts();
    }

    @Test
    void getSolvedToday_ShouldReturnTotal() {
        when(assistDao.getTotalSolvedToday()).thenReturn(12L);
        Long result = residentService.getSolvedToday();
        assertThat(result).isEqualTo(12L);
        verify(assistDao).getTotalSolvedToday();
    }

    @Test
    void getResidents_ShouldMergeLists() {
        int limit = 10;
        int skip = 0;
        String search = "test";

        List<Map<String, Object>> emergencyList = new ArrayList<>();
        Map<String, Object> res1 = new HashMap<>();
        res1.put("id", UUID.randomUUID());
        emergencyList.add(res1);

        List<Map<String, Object>> normalList = new ArrayList<>();
        Map<String, Object> res2 = new HashMap<>();
        res2.put("id", UUID.randomUUID());
        normalList.add(res2);

        when(residentDao.getResidentsWithSeverity(limit, skip, search)).thenReturn(emergencyList);
        when(residentDao.getResidentsWithoutSeverity(9, skip, search)).thenReturn(normalList);

        List<Map<String, Object>> result = residentService.getResidents(limit, skip, search);

        assertThat(result).hasSize(2);
        assertThat(result).contains(res1, res2);
        verify(residentDao).getResidentsWithSeverity(limit, skip, search);
        verify(residentDao).getResidentsWithoutSeverity(9, skip, search);
    }

    @Test
    void createResident_ShouldCreateSuccessfully() throws SQLException {
        String fullName = "John Doe";
        String cpf = "12345678900";
        char sex = 'M';
        ZonedDateTime birthDate = ZonedDateTime.now();
        String emergencyName = "Jane Doe";
        String emergencyPhone = "999999999";
        String relationship = "Wife";
        String residentialUnit = "101";
        Integer controlNumber = 123;

        when(residentDao.existsByCpf(cpf)).thenReturn(false);
        when(residentDao.existsByControlNumber(controlNumber)).thenReturn(false);
        when(residentDao.getConnection()).thenReturn(connection);
        when(residentDao.getFirstAlarmId()).thenReturn(UUID.randomUUID());

        doNothing().when(connection).setAutoCommit(false);
        doNothing().when(connection).commit();
        doNothing().when(connection).setAutoCommit(true);

        UUID result = residentService.createResident(fullName, cpf, sex, birthDate, emergencyName, emergencyPhone, relationship, residentialUnit, controlNumber);

        assertThat(result).isNotNull();
        verify(residentDao).insertResident(any(UUID.class), eq(fullName), eq(cpf), eq(sex), eq(birthDate), eq(residentialUnit));
        verify(residentDao).insertResidentEmergencyContact(any(UUID.class), any(UUID.class), eq(emergencyName), eq(emergencyPhone), eq(relationship));
        verify(residentDao).insertControlResident(any(UUID.class), eq(controlNumber), any(UUID.class), any(UUID.class));
        verify(connection).commit();
    }

    @Test
    void createResident_WhenCpfExists_ShouldThrowException() {
        String cpf = "12345678900";
        when(residentDao.existsByCpf(cpf)).thenReturn(true);

        assertThatThrownBy(() -> residentService.createResident(
                "Name", cpf, 'M', ZonedDateTime.now(), "Contact", "Phone", "Rel", "Unit", 123
        )).isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Resident with this CPF already exists");

        verify(residentDao, times(0)).getConnection();
    }

    @Test
    void createResident_WhenControlNumberExists_ShouldThrowException() {
        String cpf = "12345678900";
        Integer controlNumber = 123;
        when(residentDao.existsByCpf(cpf)).thenReturn(false);
        when(residentDao.existsByControlNumber(controlNumber)).thenReturn(true);

        assertThatThrownBy(() -> residentService.createResident(
                "Name", cpf, 'M', ZonedDateTime.now(), "Contact", "Phone", "Rel", "Unit", controlNumber
        )).isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Resident with this control number already exists");

        verify(residentDao, times(0)).getConnection();
    }

    @Test
    void createResident_TransactionRollbackOnError() throws SQLException {
        String fullName = "John Doe";
        String cpf = "12345678900";

        when(residentDao.existsByCpf(cpf)).thenReturn(false);
        when(residentDao.getConnection()).thenReturn(connection);

        doNothing().when(connection).setAutoCommit(false);

        doThrow(new IllegalArgumentException("Database error"))
                .when(residentDao).insertResident(any(), any(), any(), anyChar(), any(), any());

        assertThatThrownBy(() -> residentService.createResident(
                fullName, cpf, 'M', ZonedDateTime.now(), "Contact", "Phone", "Rel", "Unit", null
        )).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error creating resident");

        verify(connection).rollback();
    }

    @Test
    void deactivateResident_ShouldCallDao_WhenResidentExists() {
        UUID residentId = UUID.randomUUID();
        Map<String, Object> resident = new HashMap<>();
        resident.put("id", residentId);
        
        when(residentDao.getResidentDetailsById(residentId)).thenReturn(resident);
        
        residentService.deactivateResident(residentId);
        
        verify(residentDao).deactivateResident(residentId);
    }

    @Test
    void deactivateResident_ShouldThrowException_WhenResidentNotFound() {
        UUID residentId = UUID.randomUUID();
        when(residentDao.getResidentDetailsById(residentId)).thenReturn(null);
        
        assertThatThrownBy(() -> residentService.deactivateResident(residentId))
            .isInstanceOf(com.sage.exception.NotFoundException.class)
            .hasMessageContaining("Resident not found");
            
        verify(residentDao, times(0)).deactivateResident(any());
    }
}