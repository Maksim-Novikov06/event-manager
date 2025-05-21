package com.paradise.service.impl;

import com.paradise.domain.EventStatus;
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

//    @Scheduled(cron = "0 0 * * * *") // каждый час в начале часа
    @Scheduled(fixedRate = 60 * 1000) // 1 минута в миллисекундах
    public void updateEventStatus(){
        log.info("Scheduled Updater started");
        List<Long> startedEventIds = updateEventsToStarted();
        List<Long> finishedEventIds = updateEventsToFinished();
        log.info("Updated events end");
    }

    private List<Long> updateEventsToStarted() {
        log.info("Change Events from WAIT_START to STARTED");
        return eventService.changeEventStatus(EventStatus.STARTED);
    }
    private List<Long> updateEventsToFinished() {
        log.info("Change Events from STARTED to FINISHED");
        return eventService.changeEventStatus(EventStatus.FINISHED);
    }



}
