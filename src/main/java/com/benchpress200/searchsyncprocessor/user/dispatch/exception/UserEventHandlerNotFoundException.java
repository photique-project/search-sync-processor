package com.benchpress200.searchsyncprocessor.user.dispatch.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class UserEventHandlerNotFoundException extends NonRetryableEventException {
    public UserEventHandlerNotFoundException(String eventType) {
        super(String.format("User handler [%s] not found", eventType));
    }
}
