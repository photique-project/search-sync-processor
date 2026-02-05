package com.benchpress200.searchsyncprocessor.singlework.dispatch;

import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.benchpress200.searchsyncprocessor.singlework.dispatch.exception.HandlerNotFoundException;
import com.benchpress200.searchsyncprocessor.singlework.handler.SingleWorkEventHandler;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SingleWorkEventDispatcher {
    private final Map<String, SingleWorkEventHandler> handlers;

    public SingleWorkEventDispatcher(List<SingleWorkEventHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        SingleWorkEventHandler::getEventType,
                        h -> h
                ));
    }

    public void dispatch(String eventType, Long eventId, SingleWorkEventPayload payload) {
        SingleWorkEventHandler handler = handlers.get(eventType);

        if (handler == null) {
            throw new HandlerNotFoundException(eventType);
        }

        handler.handle(eventId, payload);
    }
}
