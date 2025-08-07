package com.sage.services.resident;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.model.resident.emergency.ResidentEmergencyContact;
import com.sage.port.dao.resident.ResidentEmergencyContactDao;
import com.sage.port.services.resident.ResidentEmergencyContactService;

@Service
public class ResidentEmergencyContactServiceImpl implements ResidentEmergencyContactService {

    private final ResidentEmergencyContactDao residentEmergencyContactDao;

    public ResidentEmergencyContactServiceImpl(ResidentEmergencyContactDao residentEmergencyContactDao) {
        this.residentEmergencyContactDao = residentEmergencyContactDao;
    }

    @Override
    public UUID create(CreateResidentRequestDto requestDto, UUID residentId) {
        ResidentEmergencyContact emergencyContact = ResidentEmergencyContact
                .mapFromCreateResidentRequestDto(requestDto, residentId);

        if (!emergencyContact.hasAnyFields()) {
            return null;
        }

        return this.residentEmergencyContactDao.create(emergencyContact);
    }

}
