package com.benchpress200.searchsyncprocessor.exhibition.dispatch;

import com.benchpress200.searchsyncprocessor.exhibition.consumer.payload.ExhibitionEventPayload;
import com.benchpress200.searchsyncprocessor.exhibition.dispatch.exception.ExhibitionHandlerNotFoundException;
import com.benchpress200.searchsyncprocessor.exhibition.handler.ExhibitionEventHandler;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ExhibitionEventDispatcher {
    private final Map<String, ExhibitionEventHandler> handlers;

    public ExhibitionEventDispatcher(List<ExhibitionEventHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        ExhibitionEventHandler::getEventType,
                        h -> h
                ));
    }

    public void dispatch(
            String eventType,
            Long eventId,
            ExhibitionEventPayload payload
    ) {
        ExhibitionEventHandler handler = handlers.get(eventType);

        if (handler == null) {
            throw new ExhibitionHandlerNotFoundException(eventType);
        }

        handler.handle(eventId, payload);
    }
}
