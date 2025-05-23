package com.paradise.service.impl;


import com.paradise.domain.entities.Location;
import com.paradise.repository.LocationRepository;
import com.paradise.service.LocationService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    private final LocationRepository locationRepository;

    public Location createLocation(Location location) {
        log.info("An attempt to add a location");
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
        log.info("An attempt to get all locations");
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        log.info("An attempt to get a location by id {}", id);
        return locationRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Location with id %d not found".formatted(id)));

    }


    public Location deleteLocationById(Long id) {
        log.info("An attempt to delete a location by id {}", id);
        Location locationToDelete = getLocationById(id);
        locationRepository.delete(locationToDelete);
        return locationToDelete;
    }

    public Location updateLocation(Long id, Location entity) {
        log.info("An attempt to update a location by id {}", id);
        Location locationToUpdate = getLocationById(id);

        locationToUpdate.setName(entity.getName());
        locationToUpdate.setAddress(entity.getAddress());
        locationToUpdate.setCapacity(entity.getCapacity());
        locationToUpdate.setDescription(entity.getDescription());

        return locationRepository.save(locationToUpdate);
    }
}
