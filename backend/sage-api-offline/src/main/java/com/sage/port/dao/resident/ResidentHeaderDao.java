package com.sage.port.dao.resident;

import java.util.List;

import com.sage.model.assist.SeverityLevel;
import com.sage.model.resident.ResidentHeader;

/**
 * ResidentHeaderDao provides methods to manage and retrieve resident headers,
 * including listing residents by severity level and searching for residents.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
public interface ResidentHeaderDao {

    /**
     * Lists all residents in the system, categorized by their status.
     *
     * @param severityLevel The severity level to filter residents.
     * @return List of resident headers.
     */
    List<ResidentHeader> listResidentsBySeverityLevelAssist(
            SeverityLevel severityLevel,
            int limit,
            int skip,
            String search
    );

    /**
     * Searches for residents by a search string.
     *
     * @param search The search string to filter residents.
     * @return List of resident headers matching the search criteria.
     */
    List<ResidentHeader> searchResident(
            String search
    );

}
