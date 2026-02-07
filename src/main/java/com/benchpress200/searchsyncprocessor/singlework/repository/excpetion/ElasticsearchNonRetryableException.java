package com.benchpress200.searchsyncprocessor.singlework.repository.excpetion;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class ElasticsearchNonRetryableException extends NonRetryableEventException {
    public ElasticsearchNonRetryableException(Long eventId) {
        super(String.format("Failed to update writer for singleworks: eventId=%s", eventId));
    }
}
