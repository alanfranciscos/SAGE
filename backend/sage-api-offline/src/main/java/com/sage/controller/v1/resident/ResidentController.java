package com.sage.controller.v1.resident;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sage.port.services.resident.ResidentHeaderService;

/**
 * ResidentController provides endpoints for managing residents in the system.
 * It allows for creating, updating, listing, and retrieving details of
 * residents.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/resident")
public class ResidentController {

    private final ResidentHeaderService residentHeaderService;

    public ResidentController(ResidentHeaderService residentHeaderService) {
        this.residentHeaderService = residentHeaderService;
    }

    @PostMapping
    public String createResident(@RequestBody String resident) {
        return "TODO: Implement resident creation logic";
    }

    @PatchMapping
    public String updateResident(@RequestBody String resident) {
        return "TODO: Implement resident update logic";
    }

    @GetMapping
    public Object listResidents() {
        return residentHeaderService.listResidents(10, 0); // Assuming the service returns a string representation of the list
    }

    @GetMapping("/details")
    public String getResidentDetails() {
        return "TODO: Implement resident details logic";
    }

    @GetMapping("/search")
    public String searchResident(@RequestParam(required = true) String search) {
        return "TODO: Implement resident search by ID logic";
    }

}
