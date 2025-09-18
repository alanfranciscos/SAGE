package com.sage.controller.v1.control;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sage.dto.v1.control.response.ControlListResponseDto;
import com.sage.model.resident.control.ControlResident;
import com.sage.port.services.resident.ControlResidentService;

@RestController
@RequestMapping("/v1/control")
public class ControlController {

    private final ControlResidentService controlResidentService;

    public ControlController(ControlResidentService controlResidentService) {
        this.controlResidentService = controlResidentService;
    }

    @GetMapping
    public ResponseEntity<ControlListResponseDto> listControl() {
        List<ControlResident> controlResidents = controlResidentService.listControl();
        ControlListResponseDto controlResidentDao = ControlListResponseDto.from(controlResidents);
        return ResponseEntity.ok(controlResidentDao);
    }
}
