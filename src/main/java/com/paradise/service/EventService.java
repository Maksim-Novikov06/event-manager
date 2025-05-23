package com.paradise.service;

import com.paradise.domain.EventStatus;
import com.paradise.domain.entities.Event;
import com.paradise.dto.EventDto;
import com.paradise.dto.EventSearchRequest;

import java.util.List;

public interface EventService {

    Event addEvent(Event entity);
    void deleteEventById(Long id);
    Event getEventById(Long id);
    Event update(Long id,EventDto eventToUpdate);
    List<Event> search(EventSearchRequest eventToSearch);
    List<Event> getAllEventsCreationByOwner();
    void registerUserOnEvent(Long id);
    void cancelUserByEventId(Long id);
    List<Event> getAllEventByUserRegistration();
    List<Long> changeEventStatus(EventStatus eventStatus);
    List<Long> getEventsToStarted();
    List<Long> getEventsToEnded();
    List<Event> getEventsByIds(List<Long> eventsIds);
}
