package com.benchpress200.searchsyncprocessor.exhibition.handler.exception;

import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;

public class ExhibitionSearchNotFoundException extends NonRetryableEventException {
    public ExhibitionSearchNotFoundException(Long id) {
        super(String.format("Exhibition [%s] not found", id));
    }
}
