package com.benchpress200.searchsyncprocessor.exhibition.repository;

import com.benchpress200.searchsyncprocessor.user.consumer.payload.UserEventPayload;

public interface ExhibitionSearchRepositoryCustom {
    void updateWriter(
            Long eventId,
            UserEventPayload payload
    );
}
