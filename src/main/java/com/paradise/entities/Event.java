package com.paradise.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "occupiedPlaces")
    private Integer occupiedPlaces;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "maxPlaces")
    private Integer maxPlaces;

    @Column(name = "locationId")
    private Integer locationId;

    @Column(name = "name")
    private String name;

    @Column(name = "ownerId")
    private Integer ownerId;

    @Column(name = "status")
    private Status status;


}
