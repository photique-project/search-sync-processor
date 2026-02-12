package com.benchpress200.searchsyncprocessor.common.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class OutboxPayloadDeserializationException extends NonRetryableEventException {
    public OutboxPayloadDeserializationException() {
        super("Failed to deserialize outbox event payload");
    }
}
