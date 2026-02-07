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
public class ExhibitionUpdatedViewCountEventHandler implements ExhibitionEventHandler {
    private final ExhibitionSearchRepository exhibitionSearchRepository;

    @Override
    public String getEventType() {
        return EventType.UPDATED_VIEW_COUNT;
    }

    @Override
    public void handle(
            Long eventId,
            ExhibitionEventPayload payload
    ) {
        Long exhibitionId = payload.getId();

        ExhibitionSearch exhibitionSearch = exhibitionSearchRepository.findById(exhibitionId)
                .orElseThrow(() -> new ExhibitionSearchNotFoundException(exhibitionId));

        // 조회수 이벤트의 DLT 수동 재처리로 인한 순서 꼬임은 eventId 기반으로 판단 X
        // -> 현재 조회수보다 작은 값인지 확인하고 업데이트
        // 여기서 만약 조회수 이벤트도 eventId를 갱신해준다면 일반 업데이트에서 재처리 기준을 세우기가 모호해짐
        exhibitionSearch.updateViewCount(payload);
        exhibitionSearchRepository.save(exhibitionSearch);
    }
}
