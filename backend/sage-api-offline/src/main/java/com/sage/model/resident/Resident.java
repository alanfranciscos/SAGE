package com.sage.model.resident;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Data;

/**
 * Represents a resident in the system. This class contains all the necessary
 * fields to describe a resident, including personal information, residential
 * unit, and status.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@Data
public class Resident {

    private UUID id;
    private String fullName;
    private String cpf;
    private char sex;
    private ZonedDateTime birthDate;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String residentialUnit;
    private String imageData;
    private boolean active;
}
