package com.benchpress200.searchsyncprocessor.exhibition.repository.exception;

public class ElasticsearchUpdateConflictException extends RuntimeException {
    public ElasticsearchUpdateConflictException(Long eventId, Long conflicts) {
        super(
                String.format(
                        "Writer fanout update due to version conflicts: eventId=%s, conflicts=%s",
                        eventId,
                        conflicts
                )
        );
    }
}
