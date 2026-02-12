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
public class ExhibitionViewCountIncrementedEventHandler implements ExhibitionEventHandler {
    private final ExhibitionSearchRepository exhibitionSearchRepository;

    @Override
    public String getEventType() {
        return EventType.VIEW_COUNT_INCREMENTED;
    }

    @Override
    public void handle(
            Long eventId,
            ExhibitionEventPayload payload
    ) {
        Long exhibitionId = payload.getId();

        ExhibitionSearch exhibitionSearch = exhibitionSearchRepository.findById(exhibitionId)
                .orElseThrow(() -> new ExhibitionSearchNotFoundException(exhibitionId));

        // 현재 이벤트 적용 가능 여부 확인
        if(exhibitionSearch.shouldIgnoreEvent(eventId)) {
            return;
        }

        exhibitionSearch.updateViewCount(payload);
        exhibitionSearchRepository.save(exhibitionSearch);
    }
}
