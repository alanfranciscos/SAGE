package com.sage.port.services.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AssistSseService {

    SseEmitter listEvent();
}
