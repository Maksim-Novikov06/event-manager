package com.paradise.controllers;

import com.paradise.dto.EventSearchRequest;
import com.paradise.mapper.EventMapper;
import com.paradise.dto.EventDto;
import com.paradise.domain.entities.Event;
import com.paradise.service.impl.EventServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceImpl eventServiceImpl;
    private final EventMapper eventMapper;



    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventDto> addEvent(
            @RequestBody @Valid EventDto eventToCreate
    ){

        Event createdEvent = eventServiceImpl.addEvent(
                eventMapper.toEntity(eventToCreate)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventMapper.toDto(createdEvent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER, ADMIN')")
    public ResponseEntity<Void> deleteEventById(
            @PathVariable Long id
    ){

        eventServiceImpl.deleteEventById(id);

        return ResponseEntity
                .status(HttpStatus
                        .NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER, ADMIN')")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable Long id
    ){

        Event event = eventServiceImpl.getEventById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventMapper.toDto(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER, ADMIN')")
    public ResponseEntity<EventDto> updateEventById(
            @PathVariable Long id,
            @RequestBody @Valid EventDto eventToUpdate
    ){

        Event updatedEvent = eventServiceImpl.update(id, eventToUpdate);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(eventMapper.toDto(updatedEvent));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('USER,ADMIN')")
    public ResponseEntity<List<EventDto>> getAllEvent(
            @RequestBody @Valid EventSearchRequest eventToSearch
    ){
        List<Event> eventList = eventServiceImpl.search(eventToSearch);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventList
                        .stream()
                        .map(eventMapper::toDto)
                        .toList()
                        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> findEventsByUserCreation() {

        List<Event> eventList = eventServiceImpl.getAllEventsCreationByOwner();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventList
                        .stream()
                        .map(eventMapper::toDto)
                        .toList());
    }

    @PostMapping("registrations/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> registrationUserOnEvent(
            @PathVariable("id") Long id
    ){
        eventServiceImpl.registerUserOnEvent(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @DeleteMapping("/registrations/cancel/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> cancelUserByEventId(
            @PathVariable("id") Long id
    ){
        eventServiceImpl.cancelUserByEventId(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/registrations/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<EventDto>> getAllEventsByUserRegistration(){

        List<Event> eventList= eventServiceImpl.getAllEventByUserRegistration();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventList
                        .stream()
                        .map(eventMapper::toDto)
                        .toList());

    }
}
