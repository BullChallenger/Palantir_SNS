package com.palantir.consumer;

import com.palantir.model.event.AlarmEvent;
import com.palantir.producer.AlarmProducer;
import com.palantir.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {

    private final AlarmService alarmService;

    private static final String TOPIC = "alarm";

    @KafkaListener(topics = TOPIC)
    public void consumeAlarm(AlarmEvent event, Acknowledgment ack) {
        log.info("Consume the event {}", event);
        alarmService.send(event.getAlarmType(), event.getArgs(), event.getReceiverId());
        ack.acknowledge();
    }
}
