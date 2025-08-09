package com.sage.dto.v1.resident.response;

import java.util.ArrayList;
import java.util.List;

import com.sage.model.resident.ResidentHeader;

/**
 * Data Transfer Object for representing a list of residents. This record
 * contains categorized lists of residents based on their status, along with the
 * total number of residents.
 *
 * @param severalResidents List of residents with various statuses.
 * @param warningResidents List of residents with warning statuses.
 * @param normalResidents List of residents with normal statuses.
 * @param totalResidents Total number of residents.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public record ResidentListResponseDto(
        List<ResidentHeaderResponseDto> severalResidents,
        List<ResidentHeaderResponseDto> warningResidents,
        List<ResidentHeaderResponseDto> normalResidents,
        Long totalResidents,
        Long limit,
        Long skip
        ) {

    /**
     * Creates a new instance of ResidentListResponseDto from the provided lists
     * of ResidentHeader objects.
     *
     * @param severalResidents List of residents with various statuses.
     * @param warningResidents List of residents with warning statuses.
     * @param normalResidents List of residents with normal statuses.
     * @param totalResidents Total number of residents.
     * @return A new instance of ResidentListResponseDto.
     */
    public static ResidentListResponseDto fromResidentHeaders(
            List<ResidentHeader> severalResidents,
            List<ResidentHeader> warningResidents,
            List<ResidentHeader> normalResidents,
            Long totalResidents,
            Long limit,
            Long skip
    ) {

        List<ResidentHeaderResponseDto> several = new ArrayList<>();
        for (ResidentHeader residentHeader : severalResidents) {
            several.add(new ResidentHeaderResponseDto(residentHeader));
        }
        List<ResidentHeaderResponseDto> warning = new ArrayList<>();
        for (ResidentHeader residentHeader : warningResidents) {
            warning.add(new ResidentHeaderResponseDto(residentHeader));
        }
        List<ResidentHeaderResponseDto> normal = new ArrayList<>();
        for (ResidentHeader residentHeader : normalResidents) {
            normal.add(new ResidentHeaderResponseDto(residentHeader));
        }
        return new ResidentListResponseDto(several, warning, normal, totalResidents, limit, skip);
    }

}
