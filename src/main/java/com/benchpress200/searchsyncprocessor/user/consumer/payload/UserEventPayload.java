package com.benchpress200.searchsyncprocessor.user.consumer.payload;

import lombok.Getter;

@Getter
public class UserEventPayload {
    private Long id;
    private String nickname;
    private String profileImage;
}
