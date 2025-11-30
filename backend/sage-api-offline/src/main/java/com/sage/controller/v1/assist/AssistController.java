package com.sage.controller.v1.assist;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.sage.dto.v1.assist.request.CreateAssistRequestDto;
import com.sage.dto.v1.assist.request.FinishAssistRequestDto;
import com.sage.dto.v1.assist.request.StartAssistRequestDto;
import com.sage.dto.v1.assist.response.AssistHistoryResponseDto;
import com.sage.dto.v1.assist.response.PaginatedAttendedAssistResponseDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.dto.v1.assist.response.PendingAssistDetailResponseDto;
import com.sage.port.services.assist.AssistService;

@RestController
@RequestMapping("/v1/assist")
public class AssistController {

    private final AssistService assistService;
    private static final Logger logger = Logger.getLogger(AssistController.class.getName());

    public AssistController(AssistService assistService) {
        this.assistService = assistService;
    }

    @PostMapping()
    public ResponseEntity<UUID> createAssist(
            @RequestBody CreateAssistRequestDto request
    ) {
        logger.log(Level.INFO, "Creating assist for controlId: {0}", request.controlId());

        UUID assistId = this.assistService.createAssist(request.controlId(), request.calledAt());

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(assistId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/pending")
    public ResponseEntity<PaginatedPendingAssistResponseDto> getPendingAssists(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String search
    ) {
        PaginatedPendingAssistResponseDto pendingAssists = assistService.getPendingAssists(limit, skip, search);
        return ResponseEntity.ok(pendingAssists);
    }

    @GetMapping("/pending/{assistId}")
    public ResponseEntity<PendingAssistDetailResponseDto> getPendingAssistById(
            @PathVariable UUID assistId
    ) {
        PendingAssistDetailResponseDto pendingAssist = assistService.getPendingAssistById(assistId);
        return ResponseEntity.ok(pendingAssist);
    }

    @GetMapping("/history")
    public ResponseEntity<PaginatedAttendedAssistResponseDto> getAttendedAssists(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(required = false) String search
    ) {
        PaginatedAttendedAssistResponseDto attendedAssists = assistService.getAttendedAssists(limit, skip, search);
        return ResponseEntity.ok(attendedAssists);
    }

    @GetMapping("/history/{assistId}")
    public ResponseEntity<AssistHistoryResponseDto> getAssistHistoryById(
            @PathVariable UUID assistId
    ) {
        AssistHistoryResponseDto assistHistory = assistService.getAssistHistoryById(assistId);
        return ResponseEntity.ok(assistHistory);
    }

    @PatchMapping("/{assistId}/start")
    public ResponseEntity<Void> startAssist(
            @PathVariable UUID assistId,
            @RequestBody StartAssistRequestDto request
    ) {
        assistService.startAssist(assistId, request.getCaregiverToken());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{assistId}/finish")
    public ResponseEntity<Void> finishAssist(
            @PathVariable UUID assistId,
            @RequestBody FinishAssistRequestDto request
    ) {
        assistService.finishAssist(assistId, request.getCaregiverToken(), request.getDetails());
        return ResponseEntity.noContent().build();
    }
}
