package com.sage.services.resident;

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

}
