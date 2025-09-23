package com.sage.services.resident;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sage.dao.assist.AssistDaoImpl;
import com.sage.dao.resident.ResidentDaoImpl;

@Service
public class ResidentServiceImpl {

    private final ResidentDaoImpl residentDao;
    private final AssistDaoImpl assistDao;

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

}
