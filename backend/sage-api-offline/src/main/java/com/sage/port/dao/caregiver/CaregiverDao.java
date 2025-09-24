package com.sage.port.dao.caregiver;

import java.util.UUID;

import com.sage.model.caregiver.Caregiver;

/**
 * Data Access Object interface for Caregiver operations.
 * Defines the contract for caregiver-related database operations.
 *
 * @author SAGE System
 * @version 1.0
 */
public interface CaregiverDao {
    
    /**
     * Creates a new caregiver in the database.
     *
     * @param caregiver The caregiver object to be created.
     * @return The UUID of the newly created caregiver.
     */
    UUID createCaregiver(Caregiver caregiver);
    
    /**
     * Gets the first organization ID from the database.
     *
     * @return The UUID of the first organization, or null if no organizations exist.
     */
    UUID getFirstOrganizationId();
}