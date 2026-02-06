package com.benchpress200.searchsyncprocessor.exhibition.handler;

import com.benchpress200.searchsyncprocessor.exhibition.consumer.payload.ExhibitionEventPayload;

public interface ExhibitionEventHandler {
    String getEventType();
    void handle(Long eventId, ExhibitionEventPayload payload);
}
