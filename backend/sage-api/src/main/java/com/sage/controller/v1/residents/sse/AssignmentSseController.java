package com.sage.controller.v1.residents.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sage.services.sse.CaregiverAssignmentSseService;

@RestController
@RequestMapping("/v1/sse")
public class AssignmentSseController {

    @Autowired
    private CaregiverAssignmentSseService sseService;

    @GetMapping("/assignment")
    public SseEmitter streamCaregiverAssignment() {
        return sseService.addEmitter();
    }
}
