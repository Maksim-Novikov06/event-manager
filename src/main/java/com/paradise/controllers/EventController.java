package com.paradise.controllers;

import com.paradise.converter.EventMapper;
import com.paradise.dto.EventDto;
import com.paradise.entities.Event;
import com.paradise.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;


    //create event - 1
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventDto> addEvent(
            @RequestBody @Valid EventDto eventToCreate
    ){

        Event createdEvent = eventService.addEvent(
                eventMapper.toEntity(eventToCreate)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventMapper.toDto(createdEvent));
    }



    //registration - 3
    @PostMapping("registrations/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> registrationUserByEventId(
            @PathVariable Long id
    ){
        return null;
    }

    @DeleteMapping("/registrations/cancel/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> cancelUserByEventId(
            @PathVariable Long id
    ){
        return null;
    }

    @DeleteMapping("/my/{id}")
    public ResponseEntity<?> deleteRegistrationByEventId(
            @PathVariable Long id
    ){
        return null;
    }


    //eventID - 3
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEventById(
            @PathVariable Long id
    ){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable Long id
    ){
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEventById(
            @PathVariable Long id
    ){
        return null;
    }


    //search - 1
    //веселуха напоследок
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER,ADMIN')")
    public ResponseEntity<EventDto> getEventBy(){
        return null;
    }

    //my - 1
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventDto> getMyEvents(){
        return null;
    }




}
