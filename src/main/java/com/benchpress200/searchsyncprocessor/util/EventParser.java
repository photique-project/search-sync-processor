package com.benchpress200.searchsyncprocessor.util;

import com.benchpress200.searchsyncprocessor.common.exception.OutboxPayloadDeserializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

@UtilityClass
public class EventParser {
    public static <T> T getPayload(
            ConsumerRecord<String, String> record,
            Class<T> payloadType,
            ObjectMapper objectMapper
    ) {
        try {
            return objectMapper.readValue(record.value(), payloadType);
        } catch (JsonProcessingException e) {
            throw new OutboxPayloadDeserializationException();
        }
    }

    public static Long getLongHeader(ConsumerRecord<?, ?> record, String key) {
        Header header = record.headers().lastHeader(key);

        if (header == null) {
            return null;
        }

        return Long.valueOf(new String(header.value(), StandardCharsets.UTF_8));
    }

    public static String getStringHeader(ConsumerRecord<?, ?> record, String key) {
        Header header = record.headers().lastHeader(key);

        if (header == null) {
            return null;
        }

        return new String(header.value(), StandardCharsets.UTF_8);
    }
}
