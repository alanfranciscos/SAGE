package com.sage.controller.v1.assist;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.v1.assist.request.CreateAssistRequestDto;
import com.sage.port.services.assist.AssistService;

@RestController
@RequestMapping("/v1/assist")
public class AssistController {

    private final AssistService assistService;
    private static final Logger logger = Logger.getLogger(AssistController.class.getName());

    public AssistController(AssistService assistService) {
        this.assistService = assistService;
    }

    /**
     * Creates an assist for a given control ID.
     *
     * @param controlId The control ID for which the assist is created.
     * @param request The request body containing the assist details.
     * @return A response entity with the status of the operation.
     */
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
}
