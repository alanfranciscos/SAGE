package com.sage.dto.v1.resident.response;

import java.util.UUID;

import com.sage.model.resident.ResidentHeader;

/**
 * Data Transfer Object for representing a resident's header response. This
 * record contains essential information about a resident, including their ID,
 * full name, residential unit, and image data.
 *
 * @param id The unique identifier of the resident.
 * @param fullName The full name of the resident.
 * @param residentialUnit The residential unit of the resident.
 * @param imageData The image data of the resident.
 */
public record ResidentHeaderResponseDto(UUID id,
        String fullName,
        String residentialUnit,
        String imageData
        ) {

    public ResidentHeaderResponseDto(ResidentHeader residentHeader) {
        this(
                residentHeader.getId(),
                residentHeader.getFullName(),
                residentHeader.getResidentialUnit(),
                residentHeader.getImageData()
        );
    }

}
