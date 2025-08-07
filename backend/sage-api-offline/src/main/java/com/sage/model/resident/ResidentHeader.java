package com.sage.model.resident;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static ResidentHeader mapFromResultSet(ResultSet resultSet) throws SQLException {
        ResidentHeader residentHeader = new ResidentHeader();
        residentHeader.setId(UUID.fromString(resultSet.getString("id")));
        residentHeader.setFullName(resultSet.getString("full_name"));
        residentHeader.setResidentialUnit(resultSet.getString("residential_unit"));
        residentHeader.setImageData(resultSet.getString("image_data"));
        return residentHeader;
    }
}
