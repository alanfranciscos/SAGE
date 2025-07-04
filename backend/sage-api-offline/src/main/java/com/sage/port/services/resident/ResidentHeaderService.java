package com.sage.port.services.resident;

import com.sage.dto.v1.resident.response.ResidentListResponseDto;

/**
 * ResidentHeaderService provides methods to manage and retrieve resident
 * information, including listing all residents and searching for a resident by
 * their ID.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public interface ResidentHeaderService {

    /**
     * Lists all residents in the system, categorized by their status.
     *
     * @param limit The maximum number of residents to return.
     * @param skip The number of residents to skip for pagination.
     * @return List of resident headers.
     */
    ResidentListResponseDto listResidents(int limit, int skip);

    /**
     * Searches for a resident by their ID.
     *
     * @param id The UUID of the resident to search for.
     * @return The list of residents matching the search criteria.
     */
    ResidentListResponseDto searchResident(
            String search
    );
}
