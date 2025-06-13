package com.sage.services.assist;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.sage.exception.AlreadyExistsException;
import com.sage.model.CaregiverAssignmentResident;
import com.sage.model.ControlResident;
import com.sage.model.Resident;
import com.sage.port.dao.assist.CaregiverAssignmentResidentDao;
import com.sage.port.services.assist.AssistService;
import com.sage.port.services.controlresident.ControlResidentService;
import com.sage.port.services.resident.ResidentService;

@Service
public class AssistServiceImpl implements AssistService {

    private static final Logger logger = Logger.getLogger(AssistServiceImpl.class.getName());

    private final CaregiverAssignmentResidentDao caregiverAssignmentResidentDao;

    private final ResidentService residentService;
    private final ControlResidentService controlResidentService;

    public AssistServiceImpl(
            CaregiverAssignmentResidentDao caregiverAssignmentResidentDao,
            ResidentService residentService,
            ControlResidentService controlResidentService
    ) {
        this.caregiverAssignmentResidentDao = caregiverAssignmentResidentDao;
        this.residentService = residentService;
        this.controlResidentService = controlResidentService;
    }

    @Override
    public UUID createAssist(
            String controlId,
            ZonedDateTime calledAt
    ) {

        ControlResident controlResident = controlResidentService.getControlResidentByControlId(controlId);
        UUID residentUuid = controlResident.getResidentId();

        Resident resident = residentService.getResidentById(residentUuid);

        CaregiverAssignmentResident caregiverAssignmentResident = this.caregiverAssignmentResidentDao.
                findByResidentIdAndEndAtIsNull(residentUuid).
                orElse(null);

        logger.log(Level.INFO, "Creating assist for resident: {0}", residentUuid);

        if (caregiverAssignmentResident == null) {
            caregiverAssignmentResident = new CaregiverAssignmentResident(
                    resident.getId(), calledAt, 1
            );
        } else if (caregiverAssignmentResident.getSeverityLevel() == 1) {
            caregiverAssignmentResident.setSeverityLevel(2);
        } else {
            throw new AlreadyExistsException(
                    "Assist already exists for resident: " + residentUuid
            );
        }

        try {
            caregiverAssignmentResident = this.caregiverAssignmentResidentDao.save(caregiverAssignmentResident);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating/updating assist for resident: {0}", residentUuid);
            throw new RuntimeException("Failed to create assist", e);
        }

        logger.log(
                Level.INFO,
                "Assist created/updated for resident: {0} with id {1}",
                new Object[]{residentUuid, caregiverAssignmentResident.getId()}
        );
        return caregiverAssignmentResident.getId();
    }

    @Override
    public void assignCarregiver(
            Long id,
            String carregiverId,
            ZonedDateTime assignmentAt
    ) {
        // Implementation here
    }

    @Override
    public void finishAssist(
            Long id,
            ZonedDateTime endAt
    ) {
        // Implementation here
    }
}
