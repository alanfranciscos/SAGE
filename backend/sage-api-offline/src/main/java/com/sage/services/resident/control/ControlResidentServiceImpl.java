package com.sage.services.resident.control;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.model.resident.control.ControlResident;
import com.sage.port.dao.resident.ControlResidentDao;
import com.sage.port.services.resident.ControlResidentService;

@Service
public class ControlResidentServiceImpl implements ControlResidentService {

    private final ControlResidentDao controlResidentDao;

    public ControlResidentServiceImpl(ControlResidentDao controlResidentDao) {
        this.controlResidentDao = controlResidentDao;
    }

    @Override
    public UUID create(CreateResidentRequestDto requestDto, UUID residentId) {
        ControlResident controlResident = ControlResident
                .mapFromCreateResidentRequestDto(requestDto, residentId);

        if (controlResident.getControlId() == null) {
            return null;
        }

        return this.controlResidentDao.create(controlResident);
    }

    @Override
    public boolean existsResidentByControlIdAndAlarmId(Integer controlId, String alarmId) {
        return this.controlResidentDao.existsResidentByControlIdAndAlarmId(controlId, alarmId);
    }

    @Override
    public ControlResident getByClientId(UUID residentId) {
        return this.controlResidentDao.getByClientId(residentId);
    }

}
