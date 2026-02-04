package com.benchpress200.searchsyncprocessor.singlework.consumer;

import com.benchpress200.searchsyncprocessor.common.constant.EventHeaderKey;
import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.common.exception.OutboxPayloadDeserializationException;
import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.benchpress200.searchsyncprocessor.util.EventParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SingleWorkConsumer {
    private final ObjectMapper objectMapper;

    // TODO: 데드레터 토픽에 쌓인 메시지 처리 방식 수립 필요
    @RetryableTopic(
            attempts = "${spring.kafka.consumer.retry.attempts}",
            backOff = @BackOff(
                    delay = 1000L,
                    multiplier = 2.0,
                    maxDelay = 30000L
            ),
            dltTopicSuffix = "${spring.kafka.consumer.retry.dlt-suffix}",
            exclude = { OutboxPayloadDeserializationException.class } // 역직렬화 실패는 바로 dlt
    )
    @KafkaListener(
            topics = "${spring.kafka.topics.singlework}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(
            ConsumerRecord<String, String> consumerRecord
    ) {
        Long eventId = EventParser.getLongHeader(consumerRecord, EventHeaderKey.EVENT_ID);
        String eventType = EventParser.getStringHeader(consumerRecord, EventHeaderKey.EVENT_TYPE);
        SingleWorkEventPayload payload = EventParser.getPayload(
                consumerRecord,
                SingleWorkEventPayload.class,
                objectMapper
        );

        switch (eventType) {
            case EventType.CREATED:
                break;
            case EventType.UPDATED:
                break;
            case EventType.UPDATED_VIEW_COUNT:
                break;
            case EventType.DELETED:
                break;
            default:
                // 에러 로깅
        }
    }

}
