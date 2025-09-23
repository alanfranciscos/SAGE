package com.sage.controller.v1.resident;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/v1/resident")
public class ResidentController {

    // private final ResidentService residentService;
    // public ResidentController(ResidentService residentService) {
    //     this.residentService = residentService;
    // }
    // @PostMapping
    // public ResponseEntity<UUID> createResident(@RequestBody CreateResidentRequestDto residentRequestDto) {
    //     residentRequestDto.validate();
    //     UUID residentId = residentService.createResident(residentRequestDto);
    //     final URI uri = ServletUriComponentsBuilder
    //             .fromCurrentRequest()
    //             .path("/{id}")
    //             .buildAndExpand(residentId)
    //             .toUri();
    //     return ResponseEntity.created(uri).body(residentId);
    // }
    // @PutMapping("/{id}")
    // public ResponseEntity<ResidentResponseDto> updateResident(
    //         @PathVariable UUID id,
    //         @RequestBody UpdateResidentRequestDto updateResidentRequestDto
    // ) {
    //     ResidentResponseDto updatedResident = this.residentService.updateResident(updateResidentRequestDto, id);
    //     return ResponseEntity.ok(updatedResident);
    // }
    // @GetMapping()
    // public ResponseEntity<ResidentListResponseDto> listResidents(
    //         @RequestParam(defaultValue = "10") int limit,
    //         @RequestParam(defaultValue = "0") int skip,
    //         @RequestParam(required = false) String search
    // ) {
    //     return ResponseEntity.ok(this.residentService.listResidents(limit, skip, search));
    // }
    // @GetMapping("/{id}")
    // public ResponseEntity<ResidentDetailResponseDto> getResidentDetails(@PathVariable UUID id) {
    //     return ResponseEntity.ok(this.residentService.getResidentDetailsById(id));
    // }
    private final ResidentServiceImpl residentService;

    public ResidentController(ResidentServiceImpl residentService) {
        this.residentService = residentService;
    }

    @GetMapping()
    public ResponseEntity<Object> getResidents(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(this.residentService.getResidents(limit, skip, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getResidentDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(this.residentService.getResidentDetailsById(id));
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
