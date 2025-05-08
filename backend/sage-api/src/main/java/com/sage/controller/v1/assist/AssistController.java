package com.sage.controller.v1.assist;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sage.dto.assist.CreateAssistRequest;
import com.sage.exception.AlreadyExistsException;
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
     * @param request   The request body containing the assist details.
     * @return A response entity with the status of the operation.
     */
    @PostMapping("/{controlId}")
    public ResponseEntity<String> createAssist(
            @PathVariable String controlId,
            @RequestBody CreateAssistRequest request
    ) {
        logger.log(Level.INFO, "Creating assist for controlId: {0}", controlId);

        UUID assistId;

        try {
            assistId = this.assistService.createAssist(controlId, request.calledAt());
        } catch (AlreadyExistsException e) {
            logger.log(Level.WARNING, "Assist already exists: {0}", e.getMessage());
            return ResponseEntity.status(409).body("Assist already exists");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating assist: {0}", e.getMessage());
            return ResponseEntity.status(500).body("Error creating assist");
        }

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(assistId)
                .toUri();
                
        return ResponseEntity.created(uri).build();
    }
}
