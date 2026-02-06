package com.benchpress200.searchsyncprocessor.singlework.dispatch.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class SingleWorkEventHandlerNotFoundException extends NonRetryableEventException {
    public SingleWorkEventHandlerNotFoundException(String eventType) {
        super(String.format("Singlework handler [%s] not found", eventType));
    }
}
