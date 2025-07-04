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

    public ResidentHeader mapFromResultSet(ResultSet resultSet) throws SQLException {
        this.id = UUID.fromString(resultSet.getString("id"));
        this.fullName = resultSet.getString("full_name");
        this.residentialUnit = resultSet.getString("residential_unit");
        this.imageData = resultSet.getString("image_data");
        return this;
    }
}
