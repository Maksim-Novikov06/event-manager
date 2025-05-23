package com.paradise.dto;

import com.paradise.domain.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class EventChangeKafkaMessage {

    private Long eventId;

    private Long modifierById;

    private Long ownerEventId;

    private String name;

    private Integer maxPlaces;

    private LocalDateTime date;

    private Integer cost;

    private Integer duration;

    private Long locationId;

    private EventStatus status;

    private List<Long> registrationsOnEvent;

}
