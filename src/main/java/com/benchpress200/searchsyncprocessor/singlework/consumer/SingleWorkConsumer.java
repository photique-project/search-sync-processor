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
        Long eventId = EventParser.getLongHeader(record, EventHeaderKey.EVENT_ID);
        String eventType = EventParser.getStringHeader(record, EventHeaderKey.EVENT_TYPE);

        try {
            SingleWorkEventPayload payload = EventParser.getPayload(
                    record,
                    SingleWorkEventPayload.class,
                    objectMapper
            );

            // ES에서 업데이트하는데 해당 문서 못찾았으면 생성 이벤트에서 dlt에 먼저 들어간 경우도 있기 때문에
            // dlt로 이동시킴

            // 빈으로 등록한 타입에 맞는 핸들러 찾아서 실행
            singleWorkEventDispatcher.dispatch(
                    eventType,
                    eventId,
                    payload
            );
        } catch (NonRetryableEventException e) {
            log.error(e.getMessage());
            throw e;
        }
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
