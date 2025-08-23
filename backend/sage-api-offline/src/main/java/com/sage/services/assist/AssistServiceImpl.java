package com.sage.services.assist;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
import com.sage.exception.AlreadyExistsException;
import com.sage.exception.NotFoundException;
import com.sage.model.assist.Assist;
import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.control.ControlResident;
import com.sage.port.dao.assist.AssistDao;
import com.sage.port.services.assist.AssistService;
import com.sage.port.services.resident.ControlResidentService;
import com.sage.port.services.resident.ResidentService;

@Service
public class AssistServiceImpl implements AssistService {

    private static final Logger logger = Logger.getLogger(AssistServiceImpl.class.getName());

    private final AssistDao assistDao;

    private final ResidentService residentService;
    private final ControlResidentService controlResidentService;

    public AssistServiceImpl(
            AssistDao assistDao,
            ResidentService residentService,
            ControlResidentService controlResidentService
    ) {
        this.assistDao = assistDao;
        this.residentService = residentService;
        this.controlResidentService = controlResidentService;
    }

    @Override
    public UUID createAssist(
            Integer controlId,
            ZonedDateTime calledAt
    ) {
        UUID alarmId = ControlResident.getAlarmIdDefault();
        ControlResident controlResident = controlResidentService.findByControlByIdAndAlarmId(
                controlId,
                alarmId
        );
        if (controlResident == null) {
            throw new NotFoundException("Control resident not found");
        }

        UUID residentUuid = controlResident.getResidentId();

        ResidentDetailResponseDto resident = residentService.getResidentDetailsById(residentUuid);

        Assist assist = this.assistDao.
                findByResidentIdAndEndAtIsNull(residentUuid).
                orElse(null);

        logger.log(Level.INFO, "Creating assist for resident: {0}", residentUuid);

        if (assist == null) {
            assist = new Assist(
                    resident.id(),
                    calledAt,
                    SeverityLevel.WARNING
            );
        } else if (assist.getSeverityLevel() == SeverityLevel.WARNING) {
            assist.setSeverityLevel(SeverityLevel.EMERGENCY);
        } else {
            throw new AlreadyExistsException(
                    "Assist already exists for resident: " + residentUuid
            );
        }

        try {
            UUID assistId = this.assistDao.create(assist);

            logger.log(
                    Level.INFO,
                    "Assist created/updated for resident: {0} with id {1}",
                    new Object[]{residentUuid, assistId}
            );

            return assistId;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating/updating assist for resident: {0}", residentUuid);
            throw new RuntimeException("Failed to create assist", e);
        }

    }

    @Override
    public void assignCarregiver(
            UUID id,
            UUID carregiverId,
            ZonedDateTime assignmentAt
    ) {
        // Implementation here
    }

    @Override
    public void finishAssist(
            UUID id,
            ZonedDateTime endAt
    ) {
        // Implementation here
    }
}
