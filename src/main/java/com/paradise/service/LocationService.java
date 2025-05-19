package com.paradise.service;


import com.paradise.entities.Location;
import com.paradise.repository.LocationRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);
    private final LocationRepository locationRepository;

    public Location createLocation(Location location) {
        log.info("Inside createLocation");
        if (locationRepository.existsByNameAndAddress(
                location.getName(),
                location.getAddress())
        ) {
            throw new EntityExistsException("A location with a name %s and the address %s It has already been added"
                    .formatted(location.getName(), location.getAddress()));
        }
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        Location location = locationRepository.findById(id).orElse(null);

        if (location == null) {
            throw new EntityNotFoundException("There is no location with ID = %d in Database".formatted(id));
        }

        return location;

    }


    public Location deleteLocationById(Long id) {
        log.info("Inside deleteLocationById");
        Location locationToDelete = getLocationById(id);
        locationRepository.delete(locationToDelete);
        return locationToDelete;
    }

    public Location updateLocation(Long id, Location entity) {
        log.info("Inside updateLocation");
        Location locationToUpdate = getLocationById(id);

        locationToUpdate.setName(entity.getName());
        locationToUpdate.setAddress(entity.getAddress());
        locationToUpdate.setCapacity(entity.getCapacity());
        locationToUpdate.setDescription(entity.getDescription());

        return locationRepository.save(locationToUpdate);
    }
}
