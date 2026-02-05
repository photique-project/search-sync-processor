package com.benchpress200.searchsyncprocessor.singlework.dispatch.exception;

public class HandlerNotFoundException extends RuntimeException {
    public HandlerNotFoundException(String eventType) {
        super(String.format("Handler [%s] not found", eventType));
    }
}
