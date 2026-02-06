package com.benchpress200.searchsyncprocessor.exhibition.handler.impl;

import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.exhibition.consumer.payload.ExhibitionEventPayload;
import com.benchpress200.searchsyncprocessor.exhibition.document.ExhibitionSearch;
import com.benchpress200.searchsyncprocessor.exhibition.handler.ExhibitionEventHandler;
import com.benchpress200.searchsyncprocessor.exhibition.handler.exception.ExhibitionSearchNotFoundException;
import com.benchpress200.searchsyncprocessor.exhibition.repository.ExhibitionSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExhibitionUpdatedEventHandler implements ExhibitionEventHandler {
    private final ExhibitionSearchRepository exhibitionSearchRepository;

    @Override
    public String getEventType() {
        return EventType.UPDATED;
    }

    @Override
    public void handle(
            Long eventId,
            ExhibitionEventPayload payload
    ) {
        Long exhibitionId = payload.getId();

        ExhibitionSearch exhibitionSearch = exhibitionSearchRepository.findById(exhibitionId)
                .orElseThrow(() -> new ExhibitionSearchNotFoundException(exhibitionId));

        exhibitionSearch.update(eventId, payload);
        exhibitionSearchRepository.save(exhibitionSearch);
    }
}
