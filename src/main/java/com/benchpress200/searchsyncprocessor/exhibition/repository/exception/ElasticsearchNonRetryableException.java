package com.benchpress200.searchsyncprocessor.exhibition.repository.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class ElasticsearchNonRetryableException extends NonRetryableEventException {
    public ElasticsearchNonRetryableException(Long eventId) {
        super(String.format("Failed to update writer for exhibitions: eventId=%s", eventId));
    }
}

