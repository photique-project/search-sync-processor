package com.benchpress200.searchsyncprocessor.singlework.handler.impl;

import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.benchpress200.searchsyncprocessor.singlework.document.SingleWorkSearch;
import com.benchpress200.searchsyncprocessor.singlework.handler.SingleWorkEventHandler;
import com.benchpress200.searchsyncprocessor.singlework.handler.exception.SingleWorkSearchNotFoundException;
import com.benchpress200.searchsyncprocessor.singlework.repository.SingleWorkSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SingleWorkViewCountIncrementedEventHandler implements SingleWorkEventHandler {
    private final SingleWorkSearchRepository singleWorkSearchRepository;

    @Override
    public String getEventType() {
        return EventType.VIEW_COUNT_INCREMENTED;
    }

    @Override
    public void handle(
            Long eventId,
            SingleWorkEventPayload payload
    ) {
        Long singleWorkId = payload.getId();

        SingleWorkSearch singleWorkSearch = singleWorkSearchRepository.findById(singleWorkId)
                .orElseThrow(() -> new SingleWorkSearchNotFoundException(singleWorkId));

        // 현재 이벤트 적용 가능 여부 확인
        if(singleWorkSearch.shouldIgnoreEvent(eventId)) {
            return;
        }

        singleWorkSearch.updateViewCount(payload);
        singleWorkSearchRepository.save(singleWorkSearch);
    }
}
