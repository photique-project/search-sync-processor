package com.benchpress200.searchsyncprocessor.singlework.document;

import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Document(
        indexName = "singleworks",
        writeTypeHint = WriteTypeHint.FALSE
)
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/singleworks-mappings.json")
public class SingleWorkSearch {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Object)
    private Writer writer;

    @Field(type = FieldType.Keyword, index = false)
    private String image;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private List<String> tags;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Long)
    private Long likeCount;

    @Field(type = FieldType.Long)
    private Long viewCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Long)
    private Long lastProcessedEventId;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class Writer {
        @Field(type = FieldType.Long)
        private Long id;

        @Field(type = FieldType.Keyword)
        private String nickname;

        @Field(type = FieldType.Keyword, index = false)
        private String profileImage;

        @Field(type = FieldType.Long)
        private Long lastProcessedEventId;

        public static Writer from(SingleWorkEventPayload.Writer writer) {
            return Writer.builder()
                    .id(writer.getId())
                    .nickname(writer.getNickname())
                    .profileImage(writer.getProfileImage())
                    .build();
        }
    }

    public static SingleWorkSearch of(
        Long eventId,
        SingleWorkEventPayload singleWorkEventPayload
    ) {
        return SingleWorkSearch.builder()
                .id(singleWorkEventPayload.getId())
                .writer(Writer.from(singleWorkEventPayload.getWriter()))
                .image(singleWorkEventPayload.getImage())
                .title(singleWorkEventPayload.getTitle())
                .description(singleWorkEventPayload.getDescription())
                .tags(singleWorkEventPayload.getTags())
                .category(singleWorkEventPayload.getCategory())
                .viewCount(singleWorkEventPayload.getViewCount())
                .likeCount(singleWorkEventPayload.getLikeCount())
                .createdAt(singleWorkEventPayload.getCreatedAt())
                .updatedAt(singleWorkEventPayload.getUpdatedAt())
                .lastProcessedEventId(eventId)
                .build();
    }

    public void update(
            Long eventId,
            SingleWorkEventPayload singleWorkEventPayload
    ) {
        image = singleWorkEventPayload.getImage();
        title = singleWorkEventPayload.getTitle();
        description = singleWorkEventPayload.getDescription();
        tags = singleWorkEventPayload.getTags();
        category = singleWorkEventPayload.getCategory();
        likeCount = singleWorkEventPayload.getLikeCount();
        createdAt = singleWorkEventPayload.getCreatedAt();
        updatedAt = singleWorkEventPayload.getUpdatedAt();
        lastProcessedEventId = eventId;
    }

    public void updateViewCount(SingleWorkEventPayload singleWorkEventPayload) {
        viewCount = singleWorkEventPayload.getViewCount();
    }
}
