package com.paradise.repository;

import com.paradise.domain.EventStatus;
import com.paradise.domain.entities.Event;
import com.paradise.domain.entities.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
                    select CASE WHEN COUNT(ev) > 0 THEN TRUE ELSE FALSE END
                    from events ev
                    where ev.date <= :endEventTime
                    AND ev.date  + INTERVAL '1 MINUTE' * ev.duration >= :startEventTime
                    AND ev.location_id = :locationId
            """, nativeQuery = true)
    boolean isDateForCreateEventBusy(
            @Param("startEventTime") LocalDateTime startEventTime,
            @Param("endEventTime") LocalDateTime endEventTime,
            @Param("locationId") Long locationId);


    @Modifying
    @Transactional
    @Query(value = """
                update Event ev
                set ev.eventStatus = :status
                where ev.id = :event_id
            """)
    void changeEventStatus(
            @Param("event_id") Long id,
            @Param("status") EventStatus eventStatus
    );

    List<Event> findEventsByOwnerId(Long ownerId);

    @Query(value = """
            SELECT evr FROM EventRegistration evr
            where evr.event.id = :eventId
            AND evr.userId = :userId
            """)
    Optional<EventRegistration> findRegistration(
            @Param("eventId") Long eventId,
            @Param("userId") Long userId);

    @Query("""
    select evr.event FROM EventRegistration evr
    where evr.userId = :userId

""")
    List<Event> findAllRegisteredEventsByUserId(
            @Param("userId") Long id);


    @Query("""
            SELECT ev FROM Event ev
            WHERE (:name is null or ev.name LIKE %:name%)
                AND (:placesMin is null  or ev.maxPlaces >= :placesMin)
                AND (:placesMax is null  or ev.maxPlaces <= :placesMax)
                AND (CAST(:dateStartAfter as date) is null or ev.date >= :dateStartAfter)
                AND (CAST(:dateStartBefore as date) is null or ev.date <= :dateStartBefore)
                AND (:costMin is null or ev.cost >= :costMin)
                AND (:costMax is null or ev.cost <= :costMax)
                AND (:durationMin is null or ev.duration >= :durationMin)
                AND (:durationMax is null or ev.duration <= :durationMax)
                AND (:locationId is null or ev.locationId = :locationId)
                AND (:status is null or ev.eventStatus = :status)
    """)
    List<Event> search(
            @Param("name") String name,
            @Param("placesMax") Integer placesMax,
            @Param("placesMin") Integer placesMin,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") BigDecimal costMin,
            @Param("costMax") BigDecimal costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("status") EventStatus status);

}
