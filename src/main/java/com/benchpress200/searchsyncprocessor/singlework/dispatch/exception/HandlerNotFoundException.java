package com.benchpress200.searchsyncprocessor.singlework.dispatch.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class HandlerNotFoundException extends NonRetryableEventException {
    public HandlerNotFoundException(String eventType) {
        super(String.format("Handler [%s] not found", eventType));
    }
}
