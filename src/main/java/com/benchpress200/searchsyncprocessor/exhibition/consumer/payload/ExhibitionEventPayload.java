package com.benchpress200.searchsyncprocessor.exhibition.consumer.payload;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ExhibitionEventPayload {
    private Long id;
    private Writer writer;
    private String cardColor;
    private String title;
    private String description;
    private List<String> tags;
    private Long viewCount;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    public static class Writer {
        private Long id;
        private String nickname;
        private String profileImage;
    }
}
