package com.sage.model.resident;

import java.util.UUID;

import lombok.Data;

/**
 * Represents a header for a resident, containing essential information such as
 * ID, full name, residential unit, and image data.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@Data
public class ResidentHeader {

    private UUID id;
    private String fullName;
    private String residentialUnit;
    private String imageData;

}
