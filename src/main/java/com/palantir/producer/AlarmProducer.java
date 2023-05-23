package com.palantir.producer;

import com.palantir.model.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<Long, AlarmEvent> kafkaTemplate;

    private static final String TOPIC = "alarm";

    public void send(AlarmEvent event) {
        kafkaTemplate.send(TOPIC, event.getReceiverId(), event);
        log.info("Send to Kafka finished");
    }
}
