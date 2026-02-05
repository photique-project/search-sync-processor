package com.benchpress200.searchsyncprocessor.singlework.handler;

import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.benchpress200.searchsyncprocessor.singlework.document.SingleWorkSearch;
import com.benchpress200.searchsyncprocessor.singlework.repository.SingleWorkSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SingleWorkCreatedEventHandler implements SingleWorkEventHandler{
    private final SingleWorkSearchRepository singleWorkSearchRepository;

    @Override
    public String getEventType() {
        return EventType.CREATED;
    }

    @Override
    public void handle(
            Long eventId,
            SingleWorkEventPayload payload
    ) {
        SingleWorkSearch singleWorkSearch = SingleWorkSearch.of(eventId, payload);
        singleWorkSearchRepository.save(singleWorkSearch);
    }
}
