package com.benchpress200.searchsyncprocessor.singlework.consumer;

import com.benchpress200.searchsyncprocessor.common.constant.EventHeaderKey;
import com.benchpress200.searchsyncprocessor.common.constant.EventType;
import com.benchpress200.searchsyncprocessor.common.exception.OutboxPayloadDeserializationException;
import com.benchpress200.searchsyncprocessor.singlework.consumer.payload.SingleWorkEventPayload;
import com.benchpress200.searchsyncprocessor.singlework.dispatch.SingleWorkEventDispatcher;
import com.benchpress200.searchsyncprocessor.util.EventParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SingleWorkConsumer {
    private final ObjectMapper objectMapper;
    private final SingleWorkEventDispatcher singleWorkEventDispatcher;

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
    public void consume(ConsumerRecord<String, String> record) {
        Long eventId = EventParser.getLongHeader(record, EventHeaderKey.EVENT_ID);
        String eventType = EventParser.getStringHeader(record, EventHeaderKey.EVENT_TYPE);
        SingleWorkEventPayload payload = EventParser.getPayload(
                record,
                SingleWorkEventPayload.class,
                objectMapper
        );

        // 빈으로 등록한 타입에 맞는 핸들러 찾아서 실행
        singleWorkEventDispatcher.dispatch(
                eventType,
                eventId,
                payload
        );
    }

    @DltHandler
    public void handleDltEvent(ConsumerRecord<String, String> record) {
        log.error(
                "DLT topic={}, partition={}, offset={}, key={}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key()
        );
    }
}
