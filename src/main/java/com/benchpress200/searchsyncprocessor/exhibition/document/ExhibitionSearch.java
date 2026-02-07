package com.benchpress200.searchsyncprocessor.exhibition.document;

import com.benchpress200.searchsyncprocessor.exhibition.consumer.payload.ExhibitionEventPayload;
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
        indexName = "exhibitions",
        writeTypeHint = WriteTypeHint.FALSE
)
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/exhibitions-mappings.json")
public class ExhibitionSearch {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Object)
    private Writer writer;

    @Field(type = FieldType.Keyword, index = false)
    private String cardColor;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private List<String> tags;

    @Field(type = FieldType.Long)
    private Long viewCount;

    @Field(type = FieldType.Long)
    private Long likeCount;

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

        public static Writer from(ExhibitionEventPayload.Writer writer) {
            return Writer.builder()
                    .id(writer.getId())
                    .nickname(writer.getNickname())
                    .profileImage(writer.getProfileImage())
                    .build();
        }
    }

    public static ExhibitionSearch of(
            Long eventId,
            ExhibitionEventPayload exhibitionEventPayload
    ) {
        return ExhibitionSearch.builder()
                .id(exhibitionEventPayload.getId())
                .writer(ExhibitionSearch.Writer.from(exhibitionEventPayload.getWriter()))
                .cardColor(exhibitionEventPayload.getCardColor())
                .title(exhibitionEventPayload.getTitle())
                .description(exhibitionEventPayload.getDescription())
                .tags(exhibitionEventPayload.getTags())
                .viewCount(exhibitionEventPayload.getViewCount())
                .likeCount(exhibitionEventPayload.getLikeCount())
                .createdAt(exhibitionEventPayload.getCreatedAt())
                .updatedAt(exhibitionEventPayload.getUpdatedAt())
                .lastProcessedEventId(eventId)
                .build();
    }

    public void update(
            Long eventId,
            ExhibitionEventPayload exhibitionEventPayload
    ) {
        cardColor = exhibitionEventPayload.getCardColor();
        title = exhibitionEventPayload.getTitle();
        description = exhibitionEventPayload.getDescription();
        tags = exhibitionEventPayload.getTags();
        likeCount = exhibitionEventPayload.getLikeCount();
        createdAt = exhibitionEventPayload.getCreatedAt();
        updatedAt = exhibitionEventPayload.getUpdatedAt();
        lastProcessedEventId = eventId;
    }

    public void updateViewCount(ExhibitionEventPayload exhibitionEventPayload) {
        viewCount = exhibitionEventPayload.getViewCount();
    }
}
