package com.sage.controller.v1.resident;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.port.services.resident.ResidentService;

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

    private final ResidentService residentService;

    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    @PostMapping
    public ResponseEntity<UUID> createResident(@RequestBody CreateResidentRequestDto residentRequestDto) {

        UUID residentId = residentService.createResident(residentRequestDto);

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(residentId)
                .toUri();

        return ResponseEntity.created(uri).body(residentId);

    }

    @PatchMapping
    public String updateResident(@RequestBody String resident) {
        return "TODO: Implement resident update logic";
    }

    @GetMapping()
    public ResponseEntity<ResidentListResponseDto> listResidents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip
    ) {
        return ResponseEntity.ok(residentService.listResidents(limit, skip));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidentDetailResponseDto> getResidentDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(residentService.getResidentDetailsById(id));
    }

    @GetMapping("/search")
    public String searchResident(@RequestParam(required = true) String search) {
        return "TODO: Implement resident search by ID logic";
    }

}
