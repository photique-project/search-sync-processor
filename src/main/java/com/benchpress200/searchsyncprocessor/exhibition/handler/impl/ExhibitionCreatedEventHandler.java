package com.benchpress200.searchsyncprocessor.exhibition.handler.impl;

import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.exhibition.consumer.payload.ExhibitionEventPayload;
import com.benchpress200.searchsyncprocessor.exhibition.document.ExhibitionSearch;
import com.benchpress200.searchsyncprocessor.exhibition.handler.ExhibitionEventHandler;
import com.benchpress200.searchsyncprocessor.exhibition.repository.ExhibitionSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExhibitionCreatedEventHandler implements ExhibitionEventHandler {
    private final ExhibitionSearchRepository exhibitionSearchRepository;

    @Override
    public String getEventType() {
        return EventType.CREATED;
    }

    @Override
    public void handle(
            Long eventId,
            ExhibitionEventPayload payload
    ) {
        ExhibitionSearch exhibitionSearch = ExhibitionSearch.of(eventId, payload);
        exhibitionSearchRepository.save(exhibitionSearch);
    }
}
