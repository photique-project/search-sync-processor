package com.benchpress200.searchsyncprocessor.exhibition.dispatch.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class ExhibitionHandlerNotFoundException extends NonRetryableEventException {
    public ExhibitionHandlerNotFoundException(String eventType) {
        super(String.format("Exhibition handler [%s] not found", eventType));
    }
}
