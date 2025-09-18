package com.sage.dto.v1.control.response;

import java.util.ArrayList;
import java.util.List;

import com.sage.model.resident.control.ControlResident;

public record ControlListResponseDto(
        List<ControlResponseDto> listControl
        ) {

    public static ControlListResponseDto from(
            List<ControlResident> controlResidents
    ) {
        List<ControlResponseDto> ListControlResponse = new ArrayList<>();
        for (ControlResident controlResident : controlResidents) {
            ListControlResponse.add(new ControlResponseDto(
                    controlResident.getId(),
                    controlResident.getControlId(),
                    controlResident.getResidentId()
            ));
        }
        return new ControlListResponseDto(ListControlResponse);
    }

    public ControlListResponseDto(List<ControlResponseDto> listControl) {
        this.listControl = listControl;
    }
}
