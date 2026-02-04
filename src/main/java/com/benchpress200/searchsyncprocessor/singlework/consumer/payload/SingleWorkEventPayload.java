package com.benchpress200.searchsyncprocessor.singlework.consumer.payload;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SingleWorkEventPayload {
    private Long id;
    private Writer writer;
    private String title;
    private String description;
    private List<String> tags;
    private String image;
    private String category;
    private Long viewCount;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @NoArgsConstructor
    public static class Writer {
        private Long id;
        private String nickname;
        private String profileImage;
    }
}
