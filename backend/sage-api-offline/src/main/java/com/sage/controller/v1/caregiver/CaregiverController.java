package com.sage.controller.v1.caregiver;

import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.services.caregiver.CaregiverServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/caregiver")
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

    @GetMapping
    public ResponseEntity<List<CaregiverResponseDto>> getAllCaregivers() {
        List<CaregiverResponseDto> caregivers = caregiverService.getAllCaregivers();
        return ResponseEntity.ok(caregivers);
    }
}
