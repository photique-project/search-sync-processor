package com.benchpress200.searchsyncprocessor.user.handler;

import com.benchpress200.searchsyncprocessor.user.consumer.payload.UserEventPayload;

public interface UserEventHandler {
    String getEventType();
    void handle(Long eventId, UserEventPayload payload);
}
