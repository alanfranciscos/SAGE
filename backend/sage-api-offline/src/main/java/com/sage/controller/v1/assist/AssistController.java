package com.sage.controller.v1.assist;

import com.sage.dto.v1.assist.request.CreateAssistRequestDto;
import com.sage.dto.v1.assist.response.PaginatedPendingAssistResponseDto;
import com.sage.port.services.assist.AssistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            @RequestParam(defaultValue = "0") int skip
    ) {
        PaginatedPendingAssistResponseDto pendingAssists = assistService.getPendingAssists(limit, skip);
        return ResponseEntity.ok(pendingAssists);
    }
}
