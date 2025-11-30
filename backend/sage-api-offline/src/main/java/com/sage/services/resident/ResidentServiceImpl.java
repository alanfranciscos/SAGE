package com.sage.services.resident;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sage.dao.assist.AssistDaoImpl;
import com.sage.dao.resident.ResidentDaoImpl;
import com.sage.exception.InvalidInputException;

@Service
public class ResidentServiceImpl {

    private final ResidentDaoImpl residentDao;
    private final AssistDaoImpl assistDao;

    private static final Logger logger = Logger.getLogger(ResidentServiceImpl.class.getName());

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

    public List<Map<String, Object>> getResidents(int limit, int skip, String search, Boolean active) {

        List<Map<String, Object>> emergencyResidents = this.residentDao.getResidentsWithSeverity(limit, skip, search, active);
        if (emergencyResidents != null) {
            limit -= emergencyResidents.size();
        }

        List<Map<String, Object>> normalResidents = this.residentDao.getResidentsWithoutSeverity(limit, skip, search, active);

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

    public UUID createResident(
            String fullName,
            String cpf,
            char sex,
            ZonedDateTime birthDate,
            String emergencyName,
            String emergencyPhone,
            String relationship,
            String residentialUnit,
            Integer controlNumber
    ) {

        if (residentDao.existsByCpf(cpf)) {
            throw new com.sage.exception.AlreadyExistsException("Resident with this CPF already exists.");
        }
        if (controlNumber != null && residentDao.existsByControlNumber(controlNumber)) {
            throw new com.sage.exception.AlreadyExistsException("Resident with this control number already exists.");
        }

        UUID residentId = UUID.randomUUID();
        UUID emergencyId = UUID.randomUUID();
        UUID controlId = UUID.randomUUID();

        Connection connection = residentDao.getConnection();
        try {
            connection.setAutoCommit(false);

            residentDao.insertResident(residentId, fullName, cpf, sex, birthDate, residentialUnit);

            residentDao.insertResidentEmergencyContact(emergencyId, residentId, emergencyName, emergencyPhone, relationship);

            if (controlNumber != null) {
                UUID alarmId = residentDao.getFirstAlarmId();
                residentDao.insertControlResident(controlId, controlNumber, alarmId, residentId);
            }

            connection.commit();
            return residentId;
        } catch (java.sql.SQLException | IllegalArgumentException e) {
            try {
                connection.rollback();
            } catch (java.sql.SQLException ex) {
                logger.log(Level.WARNING, "Failed to rollback transaction: {0}", ex.getMessage());
                throw new RuntimeException("Error creating resident", ex);
            }
            throw new RuntimeException("Error creating resident", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (java.sql.SQLException ex) {
                logger.log(Level.WARNING, "Failed to reset auto-commit: {0}", ex.getMessage());
                throw new RuntimeException("Error creating resident", ex);
            }
        }
    }

    public Map<String, Object> updateResident(
            UUID id,
            String fullName,
            String cpf,
            char sex,
            ZonedDateTime birthDate,
            String emergencyName,
            String emergencyPhone,
            String relationship,
            String residentialUnit,
            Integer controlNumber
    ) {

        if (residentDao.existsByCpf(cpf)) {
            Map<String, Object> resident = residentDao.getResidentDetailsById(id);
            if (!resident.get("cpf").equals(cpf)) {
                throw new com.sage.exception.AlreadyExistsException("Resident with this CPF already exists.");
            }
        }

        if (controlNumber != null && residentDao.existsByControlNumber(controlNumber)) {
            Map<String, Object> resident = residentDao.getResidentDetailsById(id);
            if (!resident.get("controlId").equals(controlNumber)) {
                throw new com.sage.exception.AlreadyExistsException("Resident with this control number already exists.");
            }
        }

        Connection connection = residentDao.getConnection();
        try {
            connection.setAutoCommit(false);

            Map<String, Object> response = residentDao.updateResident(id,
                    fullName,
                    cpf,
                    sex,
                    birthDate,
                    emergencyName,
                    emergencyPhone,
                    relationship,
                    residentialUnit,
                    controlNumber
            );

            connection.commit();
            return response;
        } catch (java.sql.SQLException | IllegalArgumentException e) {
            try {
                connection.rollback();
            } catch (java.sql.SQLException ex) {
                logger.log(Level.WARNING, "Failed to rollback transaction: {0}", ex.getMessage());
                throw new RuntimeException("Error updating resident", ex);
            }
            throw new RuntimeException("Error updating resident", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (java.sql.SQLException ex) {
                logger.log(Level.WARNING, "Failed to reset auto-commit: {0}", ex.getMessage());
                throw new RuntimeException("Error updating resident", ex);
            }
        }
    }

    public void updateResidentImage(UUID residentId, MultipartFile imageData) {
        if (imageData == null || imageData.isEmpty()) {
            throw new InvalidInputException("Image data cannot be empty.");
        }

        String originalFilename = imageData.getOriginalFilename();
        if (originalFilename == null || !isValidImageExtension(originalFilename)) {
            throw new InvalidInputException("Invalid image file type. Only PNG, JPG, and JPEG are allowed.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = residentId + extension;
        Path imagePath = Paths.get("output-files/resident-image/", newFileName);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageData.getBytes());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save image file", e);
            throw new RuntimeException("Failed to save image file", e);
        }

        residentDao.updateImageData(residentId, imagePath.toString());
    }

    public void deactivateResident(UUID residentId) {
        Map<String, Object> resident = residentDao.getResidentDetailsById(residentId);
        if (resident == null) {
            throw new com.sage.exception.NotFoundException("Resident not found with ID: " + residentId);
        }
        residentDao.deactivateResident(residentId);
    }

    private boolean isValidImageExtension(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        return lowerCaseFilename.endsWith(".png") || lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg");
    }

}
