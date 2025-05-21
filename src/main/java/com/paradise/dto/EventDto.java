package com.paradise.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class EventDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull
    @Min(value = 1, message = "Minimum maxPlaces is 1")
    private Integer maxPlaces;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotNull
    private Integer cost;

    @NotNull
    @Min(value = 30, message = "Minimum duration is 30")
    private Integer duration;


    @NotNull
    @Min(value = 1, message = "Minimum locationId is 1")
    private Long locationId;

}
