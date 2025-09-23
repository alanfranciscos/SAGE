package com.sage.services.resident;

import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.dao.assist.AssistDaoImpl;
import com.sage.dao.resident.ResidentDaoImpl;

@Service
public class ResidentServiceImpl {

    private final ResidentDaoImpl residentDao;
    private final AssistDaoImpl assistDao;

    private static final Logger logger = Logger.getLogger(ResidentServiceImpl.class.getName());

    public ResidentServiceImpl(
            ResidentDaoImpl residentDao,
            AssistDaoImpl assistDao
    ) {
        this.residentDao = residentDao;
        this.assistDao = assistDao;
    }

    public Long getCardTotal() {
        return residentDao.getCardTotal();
    }

    public Long getTotalActiveAlerts() {
        return assistDao.getTotalActiveAlerts();
    }

    public Long getSolvedToday() {
        return assistDao.getTotalSolvedToday();
    }

    public String getAssistMeanTime() {
        return assistDao.getAssistMeanTime();
    }

    public List<Map<String, Object>> getResidents(int limit, int skip, String search) {

        List<Map<String, Object>> emergencyResidents = this.residentDao.getResidentsWithSeverity(limit, skip, search);
        if (emergencyResidents != null) {
            limit -= emergencyResidents.size();
        }

        List<Map<String, Object>> normalResidents = this.residentDao.getResidentsWithoutSeverity(limit, skip, search);

        if (emergencyResidents == null) {
            emergencyResidents = new java.util.ArrayList<>();
        }
        if (normalResidents != null) {
            emergencyResidents.addAll(normalResidents);
        }

        return emergencyResidents;
    }

    public Map<String, Object> getResidentDetailsById(java.util.UUID id) {
        return this.residentDao.getResidentDetailsById(id);
    }

    public UUID createResident(
            String fullName,
            String cpf,
            char sex,
            ZonedDateTime birthDate,
            String emergencyName,
            String emergencyPhone,
            String relationship,
            String residentialUnit,
            Integer controlNumber
    ) {
        UUID residentId = UUID.randomUUID();
        UUID emergencyId = UUID.randomUUID();
        UUID controlId = UUID.randomUUID();

        Connection connection = residentDao.getConnection();
        try {
            connection.setAutoCommit(false);

            residentDao.insertResident(residentId, fullName, cpf, sex, birthDate, residentialUnit);

            residentDao.insertResidentEmergencyContact(emergencyId, residentId, emergencyName, emergencyPhone, relationship);

            if (controlNumber != null) {
                UUID alarmId = residentDao.getFirstAlarmId();
                residentDao.insertControlResident(controlId, controlNumber, alarmId, residentId);
            }

            connection.commit();
            return residentId;
        } catch (java.sql.SQLException | IllegalArgumentException e) {
            try {
                connection.rollback();
            } catch (java.sql.SQLException ex) {
                logger.log(Level.WARNING, "Failed to rollback transaction: {0}", ex.getMessage());
            }
            throw new RuntimeException("Error creating resident", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (java.sql.SQLException ex) {
                logger.log(Level.WARNING, "Failed to reset auto-commit: {0}", ex.getMessage());
            }
        }
    }

}
