package com.sage.controller.v1.sse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sage.port.services.sse.AssistSseService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/sse/assist")
public class SseAssistController {

    private final AssistSseService assistSseService;

    public SseAssistController(AssistSseService assistSseService) {
        this.assistSseService = assistSseService;
    }

    @GetMapping()
    public SseEmitter getMethodName() {
        return assistSseService.listEvent();
    }

}
