package com.paradise.service.impl;

import com.paradise.dto.EventChangeKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventKafkaProducerService {

    private final static Logger logger = LoggerFactory.getLogger(EventKafkaProducerService.class);

    private final KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate;

    public void sendEvent(EventChangeKafkaMessage kafkaEventMessage) {
        logger.info("Send kafka event message: event = {}", kafkaEventMessage);

        var res = kafkaTemplate.send(
                "change-event-topic",
                kafkaEventMessage.getOwnerEventId(),
                kafkaEventMessage
        );

        res.thenAccept(sendRes -> logger.info("Send successful"));
    }
}
