package com.paradise.service;

import com.paradise.entities.Event;
import com.paradise.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event addEvent(Event entity) {
        return null;
    }
}
