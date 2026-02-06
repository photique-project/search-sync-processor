package com.benchpress200.searchsyncprocessor.singlework.consumer;

import com.benchpress200.searchsyncprocessor.common.constant.EventHeaderKey;
import com.benchpress200.searchsyncprocessor.singlework.consumer.exception.NonRetryableEventException;
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
            exclude = { NonRetryableEventException.class }
    )
    @KafkaListener(
            topics = "${spring.kafka.topics.singlework}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, String> record) {
        long start = System.currentTimeMillis();
        Long eventId = EventParser.getLongHeader(record, EventHeaderKey.EVENT_ID);
        String eventType = EventParser.getStringHeader(record, EventHeaderKey.EVENT_TYPE);

        try {
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

            long elapsed = System.currentTimeMillis() - start;

            log.info(
                    "Consume success: eventId={}, eventType={}, aggregateId={}, topic={}, partition={}, offset={}, elapsed={}ms",
                    eventId,
                    eventType,
                    payload.getId(),
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    elapsed
            );

        } catch (NonRetryableEventException e) {
            log.error(
                    "Consume failed (non-retryable): eventId={}, eventType={}, topic={}, partition={}, offset={}, reason={}",
                    eventId,
                    eventType,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    e.getMessage(),
                    e
            );
            throw e;
        } catch (RuntimeException e) {
            log.warn(
                    "Consume failed (will retry): eventId={}, eventType={}, topic={}, partition={}, offset={}, reason={}",
                    eventId,
                    eventType,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    e.getMessage()
            );

            throw e;
        }
    }

    @DltHandler
    public void handleDltEvent(ConsumerRecord<String, String> record) {
        Long eventId = EventParser.getLongHeader(record, EventHeaderKey.EVENT_ID);
        String eventType = EventParser.getStringHeader(record, EventHeaderKey.EVENT_TYPE);

        log.error(
                "Consume sent to DLT: eventId={}, eventType={}, key={}, topic={}, partition={}, offset={}, headers={}",
                eventId,
                eventType,
                record.key(),
                record.topic(),
                record.partition(),
                record.offset(),
                record.headers()
        );
    }
}
