package com.sage.services.resident;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.ResidentHeader;
import com.sage.port.dao.resident.ResidentDao;
import com.sage.port.dao.resident.ResidentHeaderDao;
import com.sage.port.services.resident.ResidentHeaderService;

@Service
public class ResidentHeaderServiceImpl implements ResidentHeaderService {

    private static final Logger logger = Logger.getLogger(ResidentHeaderServiceImpl.class.getName());

    private final ResidentHeaderDao residentHeaderDao;
    private final ResidentDao residentDao;

    public ResidentHeaderServiceImpl(ResidentHeaderDao residentHeaderDao, ResidentDao residentDao) {
        this.residentHeaderDao = residentHeaderDao;
        this.residentDao = residentDao;
    }

    @Override
    public ResidentListResponseDto listResidents(int limit, int skip) {

        Long totalResidents = residentDao.countResidents();

        List<ResidentHeader> residentHeadersEmergency = residentHeaderDao.
                listResidentsBySeverityLevelAssist(SeverityLevel.EMERGENCY, limit, skip
                );
        if (residentHeadersEmergency != null) {
            limit -= residentHeadersEmergency.size();
        }

        if (limit <= 0) {
            return ResidentListResponseDto.fromResidentHeaders(
                    residentHeadersEmergency,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    totalResidents
            );
        }
        List<ResidentHeader> residentHeadersWarning = residentHeaderDao.
                listResidentsBySeverityLevelAssist(SeverityLevel.WARNING, limit, skip);

        if (residentHeadersWarning != null) {
            limit -= residentHeadersWarning.size();
        }

        if (limit <= 0) {
            return ResidentListResponseDto.fromResidentHeaders(
                    residentHeadersEmergency,
                    residentHeadersWarning,
                    new ArrayList<>(),
                    totalResidents
            );
        }

        List<ResidentHeader> residentHeaders = residentHeaderDao.
                listResidentsBySeverityLevelAssist(null, limit, skip);

        return ResidentListResponseDto.fromResidentHeaders(
                residentHeadersEmergency,
                residentHeadersWarning,
                residentHeaders,
                totalResidents
        );
    }

    @Override
    public ResidentListResponseDto searchResident(String search) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
