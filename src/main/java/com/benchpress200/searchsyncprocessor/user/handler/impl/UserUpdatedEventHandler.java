package com.benchpress200.searchsyncprocessor.user.handler.impl;

import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.exhibition.repository.ExhibitionSearchRepository;
import com.benchpress200.searchsyncprocessor.singlework.repository.SingleWorkSearchRepository;
import com.benchpress200.searchsyncprocessor.user.consumer.payload.UserEventPayload;
import com.benchpress200.searchsyncprocessor.user.handler.UserEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdatedEventHandler implements UserEventHandler {
    private final SingleWorkSearchRepository singleWorkSearchRepository;
    private final ExhibitionSearchRepository exhibitionSearchRepository;


    @Override
    public String getEventType() {
        return EventType.UPDATED;
    }

    @Override
    public void handle(
            Long eventId,
            UserEventPayload payload
    ) {
        // 업데이트 유저 소유 단일작품의 작가 정보 업데이트
        singleWorkSearchRepository.updateWriter(eventId, payload);

        // 업데이트 유저 소유 전시회의 작가 정보 업데이트
        exhibitionSearchRepository.updateWriter(eventId, payload);
    }
}
