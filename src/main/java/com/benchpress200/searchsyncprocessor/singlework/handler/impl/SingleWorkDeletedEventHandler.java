package com.benchpress200.searchsyncprocessor.singlework.handler.impl;

import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.benchpress200.searchsyncprocessor.singlework.handler.SingleWorkEventHandler;
import com.benchpress200.searchsyncprocessor.singlework.repository.SingleWorkSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SingleWorkDeletedEventHandler implements SingleWorkEventHandler {
    private final SingleWorkSearchRepository singleWorkSearchRepository;

    @Override
    public String getEventType() {
        return EventType.DELETED;
    }

    @Override
    public void handle(
            Long eventId,
            SingleWorkEventPayload payload
    ) {
        Long singleWorkId = payload.getId();

        singleWorkSearchRepository.findById(singleWorkId)
                .ifPresent(singleWorkSearchRepository::delete);
    }
}
