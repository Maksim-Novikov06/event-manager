package com.paradise.service;


import com.paradise.controllers.LocationController;
import com.paradise.entities.Location;
import com.paradise.repository.LocationRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);
    private final LocationRepository locationRepository;

    public Location createLocation(Location location) {
        log.info("Inside createLocation");
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(RuntimeException::new);
    }


    public Location deleteLocationById(Long id) {
        Location locationToDelete = locationRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        locationRepository.delete(locationToDelete);
        return locationToDelete;
    }

    public Location updateLocation(Long id, Location entity) {
        Location locationToUpdate = locationRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        if (entity.getCapacity() < locationToUpdate.getCapacity()) {
            throw new RuntimeException();
        }

        locationToUpdate.setName(entity.getName());
        locationToUpdate.setAddress(entity.getAddress());
        locationToUpdate.setCapacity(entity.getCapacity());
        locationToUpdate.setDescription(entity.getDescription());
        return locationRepository.save(locationToUpdate);
    }
}
