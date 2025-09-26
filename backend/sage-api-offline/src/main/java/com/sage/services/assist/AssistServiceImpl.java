package com.sage.services.assist;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.dto.v1.assist.response.AssistHistoryResponseDto;
import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.dto.v1.assist.response.PendingAssistDetailResponseDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
import com.sage.exception.AlreadyExistsException;
import com.sage.exception.InvalidInputException;
import com.sage.exception.NotFoundException;
import com.sage.model.assist.Assist;
import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.control.ControlResident;
import com.sage.port.dao.assist.AssistDao;
import com.sage.port.services.assist.AssistService;
import com.sage.port.services.caregiver.CaregiverService;
import com.sage.port.services.resident.ControlResidentService;
import com.sage.port.services.resident.ResidentService;

@Service
public class AssistServiceImpl implements AssistService {

    private static final Logger logger = Logger.getLogger(AssistServiceImpl.class.getName());

    private final AssistDao assistDao;

    private final ResidentService residentService;
    private final CaregiverService caregiverService;
    private final ControlResidentService controlResidentService;

    public AssistServiceImpl(
            AssistDao assistDao,
            ResidentService residentService,
            CaregiverService caregiverService, ControlResidentService controlResidentService
    ) {
        this.assistDao = assistDao;
        this.residentService = residentService;
        this.caregiverService = caregiverService;
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

        boolean hasAssist = assist != null;

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
            UUID assistId;
            if (hasAssist) {
                assistId = this.assistDao.update(assist);
            } else {
                assistId = this.assistDao.create(assist);
            }

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
    public void startAssist(UUID assistId, String caregiverToken) {
        CaregiverResponseDto caregiver = caregiverService.findByToken(caregiverToken);
        if (!caregiver.active()) {
            throw new InvalidInputException("Caregiver is not active.");
        }

        Assist assist = assistDao.findById(assistId)
                .orElseThrow(() -> new NotFoundException("Assist not found with id: " + assistId));

        if (assist.getAssignmentAt() != null) {
            throw new AlreadyExistsException("Assist has already been started.");
        }

        assist.setCaregiverId(caregiver.id());
        assist.setAssignmentAt(ZonedDateTime.now());

        assistDao.update(assist);
    }

    @Override
    public void finishAssist(UUID assistId, String caregiverToken, String details) {
        CaregiverResponseDto caregiver = caregiverService.findByToken(caregiverToken);

        Assist assist = assistDao.findById(assistId)
                .orElseThrow(() -> new NotFoundException("Assist not found with id: " + assistId));

        if (assist.getAssignmentAt() == null) {
            throw new InvalidInputException("Assist has not been started yet.");
        }

        if (assist.getCaregiverId() == null || !assist.getCaregiverId().equals(caregiver.id())) {
            throw new InvalidInputException("This caregiver did not start the assist.");
        }

        if (assist.getEndAt() != null) {
            throw new AlreadyExistsException("Assist has already been finished.");
        }

        assist.setEndAt(ZonedDateTime.now());
        assist.setDetail(details);

        assistDao.update(assist);
    }

    @Override
    public PaginatedPendingAssistResponseDto getPendingAssists(int limit, int skip) {
        return assistDao.getPendingAssists(limit, skip);
    }

    @Override
    public PaginatedAttendedAssistResponseDto getAttendedAssists(int limit, int skip) {
        return assistDao.getAttendedAssists(limit, skip);
    }

    @Override
    public PendingAssistDetailResponseDto getPendingAssistById(UUID assistId) {
        return assistDao.getPendingAssistById(assistId)
                .orElseThrow(() -> new NotFoundException("Pending assist not found with id: " + assistId));
    }

    @Override
    public AssistHistoryResponseDto getAssistHistoryById(UUID assistId) {
        return assistDao.getAssistHistoryById(assistId)
                .orElseThrow(() -> new NotFoundException("Assist history not found with id: " + assistId));
    }
}
