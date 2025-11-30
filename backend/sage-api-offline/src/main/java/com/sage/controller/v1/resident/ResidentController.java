package com.sage.controller.v1.resident;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.v1.resident.request.CreateResidentRequestDto;
import com.sage.dto.v1.resident.request.UpdateResidentRequestDto;
import com.sage.services.resident.ResidentServiceImpl;

/**
 * ResidentController provides endpoints for managing residents in the system.
 * It allows for creating, updating, listing, and retrieving details of
 * residents.
 *
 * @author Alan Francisco Silva
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/resident")
public class ResidentController {

    private final ResidentServiceImpl residentService;

    public ResidentController(ResidentServiceImpl residentService) {
        this.residentService = residentService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateResident(
            @PathVariable UUID id,
            @RequestBody UpdateResidentRequestDto updateResidentRequestDto
    ) {
        updateResidentRequestDto.validate();
        Map<String, Object> updatedResident = this.residentService.updateResident(
                id,
                updateResidentRequestDto.fullName(),
                updateResidentRequestDto.cpf(),
                updateResidentRequestDto.sex(),
                updateResidentRequestDto.birthDate(),
                updateResidentRequestDto.emergencyName(),
                updateResidentRequestDto.emergencyPhone(),
                updateResidentRequestDto.relationship(),
                updateResidentRequestDto.residentialUnit(),
                updateResidentRequestDto.controlNumber()
        );
        return ResponseEntity.ok(updatedResident);
    }

    @PostMapping
    public ResponseEntity<UUID> createResident(@RequestBody CreateResidentRequestDto residentRequestDto) {
        residentRequestDto.validate();

        UUID residentId = residentService.createResident(
                residentRequestDto.fullName(),
                residentRequestDto.cpf(),
                residentRequestDto.sex(),
                residentRequestDto.birthDate(),
                residentRequestDto.emergencyName(),
                residentRequestDto.emergencyPhone(),
                residentRequestDto.relationship(),
                residentRequestDto.residentialUnit(),
                residentRequestDto.controlNumber()
        );

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(residentId)
                .toUri();
        return ResponseEntity.created(uri).body(residentId);
    }

    @GetMapping()
    public ResponseEntity<Object> getResidents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok(this.residentService.getResidents(limit, skip, search, active));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getResidentDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(this.residentService.getResidentDetailsById(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateResident(@PathVariable UUID id) {
        residentService.deactivateResident(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable UUID id, @RequestParam("imageData") MultipartFile imageData) {
        residentService.updateResidentImage(id, imageData);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/card/total")
    public Map<String, Long> cardTotal() {
        Long total = residentService.getCardTotal();
        Map<String, Long> map = new HashMap<>();
        map.put("total", total);
        return map;
    }

    @GetMapping("/card/active-alerts")
    public Map<String, Long> cardActiveAlerts() {
        Long activeAlerts = residentService.getTotalActiveAlerts();
        Map<String, Long> map = new HashMap<>();
        map.put("activeAlerts", activeAlerts);
        return map;
    }

    @GetMapping("/card/solved-today")
    public Map<String, Long> cardSolvedToday() {
        Map<String, Long> map = new HashMap<>();
        Long solvedToday = residentService.getSolvedToday();
        map.put("solvedToday", solvedToday);
        return map;
    }

    @GetMapping("/card/mean-time")
    public Map<String, String> cardMeanTimeAssistToday() {
        String meanTimeAssist = residentService.getAssistMeanTime();
        Map<String, String> map = new HashMap<>();
        map.put("meanTimeAssist", meanTimeAssist);
        return map;
    }
}
