package com.benchpress200.searchsyncprocessor.singlework.handler.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class SingleWorkSearchNotFoundException extends NonRetryableEventException {
    public SingleWorkSearchNotFoundException(Long id) {
        super(String.format("Singlework [%s] not found", id));
    }
}
