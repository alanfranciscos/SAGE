package com.sage.services.resident;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.ResidentHeader;
import com.sage.port.dao.resident.ResidentDao;
import com.sage.port.dao.resident.ResidentHeaderDao;
import com.sage.port.services.helper.file.FileHelperService;
import com.sage.port.services.resident.ResidentHeaderService;

@Service
public class ResidentHeaderServiceImpl implements ResidentHeaderService {

    private final ResidentHeaderDao residentHeaderDao;
    private final ResidentDao residentDao;
    private final FileHelperService fileHelperService;

    public ResidentHeaderServiceImpl(ResidentHeaderDao residentHeaderDao, ResidentDao residentDao, FileHelperService fileHelperService) {
        this.residentHeaderDao = residentHeaderDao;
        this.residentDao = residentDao;
        this.fileHelperService = fileHelperService;
    }

    private void parseImageData(List<ResidentHeader> residentHeaders) {
        for (ResidentHeader resident : residentHeaders) {
            if (resident.getImageData() != null) {
                resident.setImageBytes(this.fileHelperService.getBase64File(resident.getImageData()).getBytes());
            }
        }
    }

    @Override
    public ResidentListResponseDto listResidents(int limit, int skip, String search) {

        Long totalResidents = residentDao.countResidents();

        List<ResidentHeader> residentHeadersEmergency = residentHeaderDao.
                listResidentsBySeverityLevelAssist(SeverityLevel.EMERGENCY, limit, skip, null);
        if (residentHeadersEmergency != null) {
            limit -= residentHeadersEmergency.size();
            this.parseImageData(residentHeadersEmergency);
        } else {
            residentHeadersEmergency = new ArrayList<>();
        }

        if (limit <= 0) {
            return ResidentListResponseDto.fromResidentHeaders(
                    residentHeadersEmergency,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    totalResidents,
                    (long) limit,
                    (long) skip
            );
        }
        List<ResidentHeader> residentHeadersWarning = residentHeaderDao.
                listResidentsBySeverityLevelAssist(SeverityLevel.WARNING, limit, skip, null);

        if (residentHeadersWarning != null) {
            limit -= residentHeadersWarning.size();
            this.parseImageData(residentHeadersWarning);
        } else {
            residentHeadersWarning = new ArrayList<>();
        }

        if (limit <= 0) {
            return ResidentListResponseDto.fromResidentHeaders(
                    residentHeadersEmergency,
                    residentHeadersWarning,
                    new ArrayList<>(),
                    totalResidents,
                    (long) limit,
                    (long) skip
            );
        }

        List<ResidentHeader> residentHeaders = residentHeaderDao.
                listResidentsBySeverityLevelAssist(null, limit, skip, search);

        if (residentHeaders != null) {
            this.parseImageData(residentHeaders);
        } else {
            residentHeaders = new ArrayList<>();
        }

        return ResidentListResponseDto.fromResidentHeaders(
                residentHeadersEmergency,
                residentHeadersWarning,
                residentHeaders,
                totalResidents,
                (long) limit,
                (long) skip
        );
    }
}
