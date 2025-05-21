package com.paradise.controllers;

import com.paradise.mapper.LocationMapper;
import com.paradise.dto.LocationDto;
import com.paradise.domain.entities.Location;
import com.paradise.service.impl.LocationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationServiceImpl locationServiceImpl;
    private final LocationMapper locationMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LocationDto> addLocation(
            @RequestBody @Valid LocationDto locationToCreate
    ) {
        Location createdLocation = locationServiceImpl.createLocation(
                locationMapper.toEntity(locationToCreate)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationMapper.toDto(createdLocation));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable Long id,
            @RequestBody @Valid LocationDto locationToUpdate
    ) {
        Location updatedLocation = locationServiceImpl.updateLocation(
                id,
                locationMapper.toEntity(locationToUpdate)
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationMapper.toDto(updatedLocation));
    }
    @PreAuthorize("hasAuthority('ADMIN, USER')")
    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> listLocations = locationServiceImpl.getAllLocations()
                .stream()
                .map(locationMapper::toDto)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listLocations);
    }

    @PreAuthorize("hasAuthority('ADMIN, USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(
            @PathVariable Long id
    ) {
        Location location = locationServiceImpl.getLocationById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationMapper.toDto(location));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<LocationDto> deleteLocation(
            @PathVariable Long id
    ) {
        Location deletedLocation = locationServiceImpl.deleteLocationById(id);

        return ResponseEntity.
                status(HttpStatus.NO_CONTENT)
                .body(locationMapper.toDto(deletedLocation));
    }
}
