package com.sage.model.resident;

import java.util.List;

import lombok.Data;

/**
 * Represents a list of residents categorized by their status. This class
 * contains three lists: - `severalResidents`: A list of all residents. -
 * `warningResidents`: A list of residents who have warnings. -
 * `normalResidents`: A list of residents who are in normal status.
 * `totalResidents`: The total number of residents across all categories.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@Data
public class ResidentList {

    private List<ResidentHeader> severalResidents;
    private List<ResidentHeader> warningResidents;
    private List<ResidentHeader> normalResidents;
    private int totalResidents;

}
