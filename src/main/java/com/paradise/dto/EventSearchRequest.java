package com.paradise.dto;

import com.paradise.domain.EventStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventSearchRequest {

    private String name;

    private Integer placesMin;

    private Integer placesMax;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateStartAfter;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateStartBefore;

    private BigDecimal costMin;

    private BigDecimal costMax;

    private Integer durationMin;

    private Integer durationMax;

    private Long locationId;

    private EventStatus status;
}
