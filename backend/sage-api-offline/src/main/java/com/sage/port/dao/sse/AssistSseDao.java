package com.sage.port.dao.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AssistSseDao {

    SseEmitter addEmitter();
}
