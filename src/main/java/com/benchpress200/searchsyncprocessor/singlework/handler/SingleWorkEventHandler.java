package com.benchpress200.searchsyncprocessor.singlework.handler;

import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;

public interface SingleWorkEventHandler {
    String getEventType();
    void handle(Long eventId, SingleWorkEventPayload payload);
}
