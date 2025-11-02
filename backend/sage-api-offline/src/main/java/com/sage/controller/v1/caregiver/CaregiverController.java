package com.sage.controller.v1.caregiver;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.request.UpdateCaregiverActiveStatusRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.services.caregiver.CaregiverServiceImpl;

@RestController
@RequestMapping("/api/v1/caregiver")
public class CaregiverController {

    private final CaregiverServiceImpl caregiverService;

    public CaregiverController(CaregiverServiceImpl caregiverService) {
        this.caregiverService = caregiverService;
    }

    @PostMapping
    public ResponseEntity<UUID> createCaregiver(@RequestBody CreateCaregiverRequestDto caregiverRequestDto) {
        caregiverRequestDto.validate();

        UUID caregiverId = caregiverService.createCaregiver(
                caregiverRequestDto.fullName(),
                caregiverRequestDto.cpf(),
                caregiverRequestDto.email(),
                caregiverRequestDto.phone(),
                caregiverRequestDto.position()
        );

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(caregiverId)
                .toUri();
        return ResponseEntity.created(uri).body(caregiverId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCaregiver(@PathVariable UUID id, @RequestBody CreateCaregiverRequestDto requestDto) {
        requestDto.validate();
        caregiverService.updateCaregiver(id, requestDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> updateCaregiverActiveStatus(
            @PathVariable UUID id,
            @RequestBody UpdateCaregiverActiveStatusRequestDto request) {

        caregiverService.updateCaregiverActiveStatus(id, request.active());
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<CaregiverResponseDto>> getAllCaregivers(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String search
    ) {
        List<CaregiverResponseDto> caregivers = caregiverService.getAllCaregivers(limit, skip, search);
        return ResponseEntity.ok(caregivers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaregiverResponseDto> getCaregiverById(@PathVariable UUID id) {
        CaregiverResponseDto caregiver = caregiverService.getCaregiverById(id);
        return ResponseEntity.ok(caregiver);
    }

    @GetMapping("/count-caregiver-leader")
    public Map<String, Long> getCountCaregiverLeader() {
        Long total = caregiverService.getCountCaregiverLeader();
        Map<String, Long> map = new HashMap<>();
        map.put("total", total);
        return map;
    }

    @GetMapping("/verify-active-caregiver")
    public boolean varifyIfHasOnlyOneActiveCaregiver() {
        boolean existeAtivo = false;
        for (CaregiverResponseDto c : caregiverService.getAllCaregivers()) {
            if (c.active()) {
                existeAtivo = true;
                break;
            }
        }
        return existeAtivo;
    }

}
