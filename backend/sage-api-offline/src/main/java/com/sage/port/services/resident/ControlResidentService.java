package com.sage.port.services.resident;

import java.util.List;
import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.model.resident.control.ControlResident;

public interface ControlResidentService {

    /**
     * Creates a new resident emergency contact based on the provided request
     * DTO.
     *
     * @param requestDto The DTO containing the details of the emergency contact
     * to be created.
     * @param residentId The UUID of the resident to whom the emergency contact
     * belongs.
     * @return The UUID of the newly created emergency contact.
     */
    UUID create(
            CreateResidentRequestDto requestDto, UUID residentId
    );

    /**
     * Checks if a resident exists by their CPF (Cadastro de Pessoas Físicas).
     *
     * @param cpf The CPF of the resident to check.
     * @return True if a resident with the given CPF exists, false otherwise.
     */
    boolean existsResidentByControlIdAndAlarmId(Integer controlId, String alarmId);

    /**
     * Retrieves a control resident by its control ID and alarm ID.
     *
     * @param controlId The control ID of the resident to retrieve.
     * @param alarmId The alarm ID of the resident to retrieve.
     * @return The ControlResident object associated with the given control ID
     * and alarm ID.
     */
    ControlResident findByControlByIdAndAlarmId(
            Integer controlId,
            UUID alarmId
    );

    /**
     * Retrieves a control resident by its client ID.
     *
     * @param residentId The UUID of the resident to retrieve.
     * @return The ControlResident object associated with the given resident ID.
     */
    ControlResident getByResidentId(UUID residentId);

    /**
     * Lists all control residents.
     *
     * @return A list of all ControlResident objects.
     */
    List<ControlResident> listControl();

    /**
     * Updates an existing control resident.
     *
     * @param id
     */
    void update(UpdateResidentRequestDto requestDto, UUID id);

}
