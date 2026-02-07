package com.benchpress200.searchsyncprocessor.user.dispatch;

import com.benchpress200.searchsyncprocessor.user.consumer.payload.UserEventPayload;
import com.benchpress200.searchsyncprocessor.user.dispatch.exception.UserEventHandlerNotFoundException;
import com.benchpress200.searchsyncprocessor.user.handler.UserEventHandler;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UserEventDispatcher {
    private final Map<String, UserEventHandler> handlers;

    public UserEventDispatcher(List<UserEventHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        UserEventHandler::getEventType,
                        h -> h
                ));
    }

    public void dispatch(
            String eventType,
            Long eventId,
            UserEventPayload payload
    ) {
        UserEventHandler handler = handlers.get(eventType);

        if (handler == null) {
            throw new UserEventHandlerNotFoundException(eventType);
        }

        handler.handle(eventId, payload);
    }
}
