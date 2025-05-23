package com.paradise.service.impl;

import com.paradise.domain.EventStatus;
import com.paradise.domain.entities.EventRegistration;
import com.paradise.dto.EventChangeKafkaMessage;
import com.paradise.service.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final EventService eventService;
    private final EventKafkaProducerService eventKafkaProducerService;

//    @Scheduled(cron = "0 0 * * * *") // каждый час в начале часа
    @Scheduled(fixedRate = 60 * 1000) // 1 минута в миллисекундах
    public void updateEventStatus(){
        log.info("Scheduled Updater started");

        List<Long> startedEventIds = updateEventsToStarted();
        if(!startedEventIds.isEmpty()){
            sendEventStatusUpdatesToKafka(startedEventIds);
            log.info("Change Events from WAIT_START to STARTED {}", startedEventIds);
        }
        List<Long> finishedEventIds = updateEventsToFinished();
        if(!finishedEventIds.isEmpty()){
            sendEventStatusUpdatesToKafka(finishedEventIds);
            log.info("Change Events from STARTED to FINISHED {}", finishedEventIds);
        }
        log.info("Updated events end");
    }

    private List<Long> updateEventsToStarted() {

        return eventService.changeEventStatus(EventStatus.STARTED);
    }
    private List<Long> updateEventsToFinished() {

        return eventService.changeEventStatus(EventStatus.FINISHED);
    }

    private void sendEventStatusUpdatesToKafka(
            List<Long> eventsIds
    ) {

        log.info("Sending {} events to Kafka", eventsIds.size());

        eventService.getEventsByIds(eventsIds)
                .forEach(event -> eventKafkaProducerService.sendEvent(
                        new EventChangeKafkaMessage(
                                event.getId(),
                                null,
                                event.getOwnerId(),
                                event.getName(),
                                event.getMaxPlaces(),
                                event.getDate(),
                                event.getCost(),
                                event.getDuration(),
                                event.getLocationId(),
                                event.getEventStatus(),
                                event.getEventRegistrations()
                                        .stream()
                                        .map(EventRegistration::getUserId)
                                        .toList()
                        )
                ));
    }



}
