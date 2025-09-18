package com.sage.port.services.resident;

import java.util.UUID;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
import com.sage.dto.v1.resident.response.ResidentResponseDto;

/**
 * ResidentService provides methods to manage residents in the system. It allows
 * for creating, updating, deleting, and retrieving resident information.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public interface ResidentService extends ResidentHeaderService {

    /**
     * Creates a new resident with the provided details.
     *
     * @param requestDto The request DTO containing the details of the resident
     * to create.
     * @return The UUID of the newly created resident.
     */
    UUID createResident(
            CreateResidentRequestDto requestDto
    );

    /**
     * Updates an existing resident with the provided details.
     *
     * @param requestDto The request DTO containing the updated details of the
     * resident.
     * @return The updated resident object.
     */
    ResidentResponseDto updateResident(
            UpdateResidentRequestDto requestDto,
            UUID id
    );

    /**
     * Get a resident by their UUID.
     *
     * @param id The UUID of the resident to get.
     * @return ResidentDetailResponseDto containing the details of the resident.
     */
    ResidentDetailResponseDto getResidentDetailsById(
            UUID id
    );
}
