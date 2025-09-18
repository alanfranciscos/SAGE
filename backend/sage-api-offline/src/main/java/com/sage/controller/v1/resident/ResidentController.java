package com.sage.controller.v1.resident;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.dto.v1.resident.response.ResidentDetailResponseDto;
import com.sage.dto.v1.resident.response.ResidentListResponseDto;
import com.sage.dto.v1.resident.response.ResidentResponseDto;
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

        residentRequestDto.validate();
        UUID residentId = residentService.createResident(residentRequestDto);

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(residentId)
                .toUri();

        return ResponseEntity.created(uri).body(residentId);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResidentResponseDto> updateResident(
            @PathVariable UUID id,
            @RequestBody UpdateResidentRequestDto updateResidentRequestDto
    ) {
        ResidentResponseDto updatedResident = this.residentService.updateResident(updateResidentRequestDto, id);
        return ResponseEntity.ok(updatedResident);
    }

    @GetMapping()
    public ResponseEntity<ResidentListResponseDto> listResidents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(this.residentService.listResidents(limit, skip, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidentDetailResponseDto> getResidentDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(this.residentService.getResidentDetailsById(id));
    }

    @GetMapping("/card/total")
    public Map<String, Long> cardTotal() {
        Map<String, Long> map = new HashMap<>();
        map.put("total", 7L);
        return map;
    }

    @GetMapping("/card/alerts")
    public Map<String, Long> cardAlerts() {
        Map<String, Long> map = new HashMap<>();
        map.put("alerts", 1L);
        return map;
    }

    @GetMapping("/card/resolved")
    public Map<String, Long> cardResolved() {
        Map<String, Long> map = new HashMap<>();
        map.put("solved_today", 7L);
        return map;
    }

    @GetMapping("/card/mean-time")
    public Map<String, Long> cardMeanTime() {
        Map<String, Long> map = new HashMap<>();
        map.put("mean_time", 7L);
        return map;
    }
}
