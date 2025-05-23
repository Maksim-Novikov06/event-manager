package com.paradise.service.impl;

import com.paradise.domain.EventStatus;
import com.paradise.domain.entities.Event;
import com.paradise.domain.entities.EventRegistration;
import com.paradise.domain.entities.Location;
import com.paradise.domain.entities.User;
import com.paradise.dto.EventChangeKafkaMessage;
import com.paradise.dto.EventDto;
import com.paradise.dto.EventSearchRequest;
import com.paradise.repository.EventRepository;
import com.paradise.repository.RegistrationRepository;
import com.paradise.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final LocationServiceImpl locationService;
    private final UserServiceImpl userService;
    private final EventKafkaProducerService eventKafkaProducerService;
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);


    public Event addEvent(Event entity) {
        logger.info("Attempt to add an event: {}", entity);

        User currentUser = getCurrentAuthUser();

        User userOwner = userService.getByLogin(currentUser.getLogin());

        Location location = locationService.getLocationById(entity.getLocationId());

        if (entity.getDate() == null || entity.getDuration() == null) {
            throw new IllegalArgumentException("Event date and duration must be provided");
        }

        if (location == null) {
            throw new IllegalArgumentException("Location with id %d not found".formatted(entity.getLocationId()));
        }

        if (location.getCapacity() < entity.getMaxPlaces()) {
            logger.error("The capacity of the location is less than the number of participants in the event");
            throw new IllegalArgumentException("Location capacity: %d, max places: %d"
                    .formatted(location.getCapacity(), entity.getMaxPlaces()));
        }

        LocalDateTime start = entity.getDate();
        LocalDateTime end = start.plusMinutes(entity.getDuration());

        logger.info("Checking for the presence of an event at a given location during the event being added");
        boolean isBusy = eventRepository.isDateForCreateEventBusy(start, end, entity.getLocationId());
        if (isBusy) {
            logger.warn("Event conflict detected");
            throw new IllegalArgumentException("Event already exists at locationId=%d on date=%s"
                    .formatted(entity.getLocationId(), start));
        }

        Event eventToSave = new Event(
                null,
                entity.getName(),
                userOwner.getId(),
                entity.getMaxPlaces(),
                new ArrayList<>(),
                entity.getDate(),
                entity.getCost(),
                entity.getDuration(),
                entity.getLocationId(),
                EventStatus.WAIT_START);

        return eventRepository.save(eventToSave);
    }



    public void deleteEventById(Long id) {
        logger.info("Attempt to delete an event with id: {}", id);

        Event eventToCancel = getEventById(id);
        User currentUser = getCurrentAuthUser();

        if (eventToCancel.getEventStatus() != EventStatus.WAIT_START) {
            logger.error("Cannot modify event in status = {}", eventToCancel.getEventStatus());
            throw new IllegalArgumentException("The event has a status %s. It cannot be canceled"
                    .formatted(eventToCancel.getEventStatus()));
        }
        if (!eventToCancel.getOwnerId().equals(currentUser.getId())) {
            logger.error("User with id {} is not the owner of this event", currentUser.getId());
            throw new IllegalArgumentException("User can't modify this event");
        }
        eventRepository.changeEventStatus(eventToCancel.getId(), EventStatus.CANCELLED);
    }

    private static User getCurrentAuthUser() {
        logger.info("Trying to find the current authorized user");

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(user instanceof User)) {
            throw new AccessDeniedException("Invalid username or password");
        }

        return (User) user;
    }

    public Event getEventById(Long id) {
        logger.info("Attempt to receive an event by id");

        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with id %d not found".formatted(id)));
    }

    public Event update(Long id, EventDto eventDto) {
        logger.info("Attempt to update the event");

        User currentUser = getCurrentAuthUser();
        Event eventToUpdate = getEventById(id);


        if(!eventToUpdate.getOwnerId().equals(currentUser.getId())){
            logger.info("User with id {} is not the owner of this event", currentUser.getId());
            throw new AccessDeniedException("User with login: %s cannot modify this event".formatted(currentUser.getLogin()));
        }

        if (eventToUpdate.getEventStatus() != EventStatus.WAIT_START) {
            logger.error("The event has a status {}. It cannot be updated}", eventToUpdate.getEventStatus());
        }

        if (eventToUpdate.getMaxPlaces() != null || eventToUpdate.getLocationId() != null) {
            Long locationId = eventToUpdate.getLocationId();
            Integer maxPlaces = eventToUpdate.getMaxPlaces();
            Location location = locationService.getLocationById(locationId);

            if (location.getCapacity() < maxPlaces){
                logger.error("Location capacity exceeded");
                throw new IllegalArgumentException("Location capacity: %d, max places: %d");
            }
        }

        if (eventToUpdate.getMaxPlaces() != null
                && eventToUpdate.getEventRegistrations().size() > eventToUpdate.getMaxPlaces()){
            logger.info("Max places exceeded");
            throw new IllegalArgumentException("Max places exceeded");
        }



        eventToUpdate.setName(eventDto.getName());
        eventToUpdate.setDate(eventDto.getDate());
        eventToUpdate.setCost(eventDto.getCost());
        eventToUpdate.setDuration(eventDto.getDuration());
        eventToUpdate.setMaxPlaces(eventDto.getMaxPlaces());

        eventRepository.save(eventToUpdate);

        EventChangeKafkaMessage eventChangeKafkaMessage = getEventChangeKafkaMessage(eventToUpdate, eventDto, currentUser);
        eventKafkaProducerService.sendEvent(eventChangeKafkaMessage);

        return eventToUpdate;
    }




    public List<Event> search(@Valid EventSearchRequest eventSearchRequest) {
        logger.info("An attempt to perform a filter search");
        return eventRepository.search(
                eventSearchRequest.getName(),
                eventSearchRequest.getPlacesMax(),
                eventSearchRequest.getPlacesMin(),
                eventSearchRequest.getDateStartAfter(),
                eventSearchRequest.getDateStartBefore(),
                eventSearchRequest.getCostMin(),
                eventSearchRequest.getCostMax(),
                eventSearchRequest.getDurationMin(),
                eventSearchRequest.getDurationMax(),
                eventSearchRequest.getLocationId(),
                eventSearchRequest.getStatus()
        );

    }

    public List<Event> getAllEventsCreationByOwner() {
        logger.info("An attempt to receive events created by the currently authorized user");
        User currentUser = getCurrentAuthUser();
        return eventRepository.findEventsByOwnerId(currentUser.getId());
    }

    public void registerUserOnEvent(Long id) {
        logger.info("An attempt to register a currently authorized user for an event");
        User currentUser = getCurrentAuthUser();
        Event event = getEventById(id);

        if (event.getOwnerId().equals(currentUser.getId())) {
            logger.info("User with id {} - owner", currentUser.getId());
            throw new IllegalArgumentException("The owner cannot register for his own event=%s".formatted(event));
        }

        Optional<EventRegistration> registration =
                eventRepository.findRegistration(event.getId(), currentUser.getId());
        if (registration.isPresent()) {
            logger.info("User with id {} - already registered", currentUser.getId());
            throw new IllegalArgumentException("User with id %d - already registered"
                    .formatted(currentUser.getId()));
        }

        if (event.getEventStatus() != EventStatus.WAIT_START) {
            logger.error("Cannot register event in status = {}", event.getEventStatus());
            throw new IllegalArgumentException("Event has status: %s".formatted(event.getEventStatus()));
        }

        if (event.getMaxPlaces() <= event.getEventRegistrations().size()) {
            logger.error("An attempt to register a user. Current number of registered users: {}", event.getEventRegistrations().size());
            throw new IllegalArgumentException("Max places exceeded");
        }


        EventRegistration evr = new EventRegistration(
                null,
                currentUser.getId(),
                event,
                LocalDateTime.now()
        );

        registrationRepository.save(evr);

    }

    public void cancelUserByEventId(Long id) {
        logger.info("Attempt to cancel an event by id");
        User currentUser = getCurrentAuthUser();
        EventRegistration eventRegistration = eventRepository.findRegistration(id, currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Event with id %d not found".formatted(id)));

        if (eventRegistration.getEvent().getEventStatus() !=  EventStatus.WAIT_START) {
            logger.error("Cannot cancel event in status = {}", eventRegistration.getEvent().getEventStatus());
            throw new IllegalArgumentException("Event has status: %s".formatted(eventRegistration.getEvent().getEventStatus()));
        }
        registrationRepository.delete(eventRegistration);

    }

    public List<Event> getAllEventByUserRegistration() {
        logger.info("An attempt to get all the events that the currently logged in user is registered for");
        User currentUser = getCurrentAuthUser();
        return eventRepository.findAllRegisteredEventsByUserId(currentUser.getId());
    }

    @Transactional
    @Override
    public List<Long> changeEventStatus(EventStatus eventStatus) {
        if (eventStatus == EventStatus.STARTED) {
            List<Long> eventsToStarted = getEventsToStarted();
            if (!eventsToStarted.isEmpty()) {
                eventRepository.changeEventStatus(eventsToStarted, eventStatus);
            }
            return eventsToStarted;
        }
        if (eventStatus == EventStatus.FINISHED) {
            List<Long> eventsToEnded = getEventsToEnded();
            if (!eventsToEnded.isEmpty()) {
                eventRepository.changeEventStatus(eventsToEnded, eventStatus);
            }
            return eventsToEnded;
        }
        return List.of();
    }

    public List<Long> getEventsToStarted() {
        return eventRepository.getStatedEventsWithStatus(EventStatus.WAIT_START);
    }

    public List<Long> getEventsToEnded() {
        return eventRepository.getEndedEventsWithStatus(EventStatus.STARTED.name());
    }

    @Override
    public List<Event> getEventsByIds(List<Long> eventsIds) {
        return eventRepository.findAllEventsByIds(eventsIds);
    }

    private EventChangeKafkaMessage getEventChangeKafkaMessage(
            Event eventToUpdate,
            EventDto eventDto,
            User currentUser
    ) {
        return new EventChangeKafkaMessage(
                eventToUpdate.getId(),
                currentUser.getId(),
                eventToUpdate.getOwnerId(),
                eventDto.getName(),
                eventDto.getMaxPlaces(),
                eventDto.getDate(),
                eventDto.getCost(),
                eventDto.getDuration(),
                eventDto.getLocationId(),
                eventToUpdate.getEventStatus(),
                eventToUpdate.getEventRegistrations()
                        .stream()
                        .map(EventRegistration::getUserId)
                        .toList()

        );
    }



}
