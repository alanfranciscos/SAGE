package com.sage.services.resident;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.dto.v1.resident.response.ResidentResponseDto;
import com.sage.exception.AlreadyExistsException;
import com.sage.exception.NotFoundException;
import com.sage.model.resident.Resident;
import com.sage.model.resident.control.ControlResident;
import com.sage.model.resident.emergency.ResidentEmergencyContact;
import com.sage.port.dao.resident.ResidentDao;
import com.sage.port.services.resident.ControlResidentService;
import com.sage.port.services.resident.ResidentEmergencyContactService;
import com.sage.port.services.resident.ResidentHeaderService;
import com.sage.port.services.resident.ResidentService;

@Service
public class OldResidentServiceImpl implements ResidentService {

    private final ResidentHeaderService residentHeaderService;
    private final ResidentDao residentDao;
    private final ResidentEmergencyContactService residentEmergencyContactService;
    private final ControlResidentService controlResidentService;

    public OldResidentServiceImpl(
            ResidentHeaderService residentHeaderService,
            ResidentDao residentDao,
            ResidentEmergencyContactService residentEmergencyContactService,
            ControlResidentService controlResidentService
    ) {
        this.residentHeaderService = residentHeaderService;
        this.residentDao = residentDao;
        this.residentEmergencyContactService = residentEmergencyContactService;
        this.controlResidentService = controlResidentService;
    }

    @Override
    public ResidentListResponseDto listResidents(int limit, int skip, String search) {
        return this.residentHeaderService.listResidents(limit, skip, search);
    }

    @Override
    public UUID createResident(CreateResidentRequestDto requestDto) {
        Resident resident = Resident.mapFromCreateResidentRequestDto(requestDto);

        String alreadyExistsMessage = "";
        if (this.residentDao.existsResidentByCpf(resident.getCpf())) {
            alreadyExistsMessage = "Resident with CPF " + resident.getCpf() + " already exists.\n";
        }

        if (this.controlResidentService.existsResidentByControlIdAndAlarmId(
                requestDto.controlNumber(),
                ControlResident.getAlarmIdDefault().toString()
        )) {
            alreadyExistsMessage += "Control resident with control ID " + requestDto.controlNumber() + " already exists.\n";
        }

        if (alreadyExistsMessage.endsWith("\n")) {
            alreadyExistsMessage = alreadyExistsMessage.substring(0, alreadyExistsMessage.length() - 1);
        }

        if (!alreadyExistsMessage.isEmpty()) {
            throw new AlreadyExistsException(alreadyExistsMessage);
        }

        ZonedDateTime now = ZonedDateTime.now();
        resident.setCreatedAt(now);
        resident.setUpdatedAt(now);

        UUID residentId = this.residentDao.createResident(resident);

        // if (!requestDto.imageData().isEmpty()) {
        //     String imagePath = this.fileHelperService.saveBase64File(
        //             requestDto.imageData().get(),
        //             FileType.RESIDENT_IMAGE, residentId.toString()
        //     );
        //     this.residentDao.updateImageData(residentId, imagePath);
        // }
        this.residentEmergencyContactService.create(requestDto, residentId);

        this.controlResidentService.create(requestDto, residentId);
        return residentId;
    }

    @Override
    public ResidentResponseDto updateResident(UpdateResidentRequestDto requestDto, UUID id) {
        Resident resident = this.residentDao.findResidentById(id);
        if (resident == null) {
            throw new NotFoundException("Resident not found with ID: " + id);
        }

        if (!resident.getCpf().equals(requestDto.cpf())
                && this.residentDao.existsResidentByCpf(requestDto.cpf())) {
            throw new AlreadyExistsException("Resident with CPF " + requestDto.cpf() + " already exists.");
        }

        resident = Resident.mapFromUpdateResidentRequestDto(requestDto);

        resident.setId(id);
        resident.setUpdatedAt(ZonedDateTime.now());
        // this.residentDao.updateResident(resident);
        // if (requestDto.imageData().isPresent() && !requestDto.imageData().get().isEmpty()) {
        //     String imagePath = this.fileHelperService.saveBase64File(
        //             requestDto.imageData().get(),
        //             FileType.RESIDENT_IMAGE, id.toString()
        //     );
        //     this.residentDao.updateImageData(id, imagePath);
        // }

        this.residentEmergencyContactService.update(requestDto, id);
        this.controlResidentService.update(requestDto, id);

        ResidentResponseDto response = new ResidentResponseDto(
                resident.getId(),
                resident.getFullName(),
                resident.getCpf(),
                resident.getSex(),
                resident.getBirthDate(),
                resident.getCreatedAt(),
                resident.getUpdatedAt(),
                resident.getResidentialUnit(),
                resident.getImageData(),
                resident.isActive()
        );
        return response;
    }

    @Override
    public void updateResidentImage(UUID id, MultipartFile imageData) {
        throw new UnsupportedOperationException("This method is not supported in OldResidentServiceImpl.");
    }

    @Override
    public ResidentDetailResponseDto getResidentDetailsById(UUID id) {
        Resident resident = this.residentDao.findResidentById(id);
        if (resident == null) {
            throw new NotFoundException("Resident not found with ID: " + id);
        }
        // byte[] imageData = null;
        // if (resident.getImageData() != null) {
        //     imageData = this.fileHelperService.getBase64File(resident.getImageData()).getBytes();
        // }

        ResidentEmergencyContact residentEmergencyContact = this.residentEmergencyContactService.getByClientId(resident.getId());
        ControlResident controlResident = this.controlResidentService.getByResidentId(resident.getId());
        ResidentDetailResponseDto response = new ResidentDetailResponseDto(
                resident.getId(),
                resident.getFullName(),
                resident.getCpf(),
                resident.getSex(),
                resident.getBirthDate(),
                resident.getCreatedAt(),
                resident.getUpdatedAt(),
                resident.isActive(),
                null,
                residentEmergencyContact != null ? residentEmergencyContact.getFullName() : null,
                residentEmergencyContact != null ? residentEmergencyContact.getPhone() : null,
                residentEmergencyContact != null ? residentEmergencyContact.getRelationship() : null,
                resident.getResidentialUnit(),
                controlResident != null ? controlResident.getControlId() : null
        );
        return response;
    }

}
