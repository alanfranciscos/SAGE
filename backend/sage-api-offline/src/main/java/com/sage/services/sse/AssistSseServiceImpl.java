package com.sage.services.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sage.port.dao.sse.AssistSseDao;
import com.sage.port.services.sse.AssistSseService;

@Service
public class AssistSseServiceImpl implements AssistSseService {

    final private AssistSseDao assistSseDao;

    public AssistSseServiceImpl(AssistSseDao assistSseDao) {
        this.assistSseDao = assistSseDao;
    }

    @Override
    public SseEmitter listEvent() {
        return this.assistSseDao.addEmitter();
    }
}
