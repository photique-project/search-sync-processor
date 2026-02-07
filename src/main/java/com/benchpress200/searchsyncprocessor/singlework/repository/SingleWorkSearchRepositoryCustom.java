package com.benchpress200.searchsyncprocessor.singlework.repository;

import com.benchpress200.searchsyncprocessor.user.consumer.payload.UserEventPayload;

public interface SingleWorkSearchRepositoryCustom {
    void updateWriter(
            Long eventId,
            UserEventPayload payload
    );
}
