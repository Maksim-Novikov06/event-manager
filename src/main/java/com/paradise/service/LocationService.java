package com.paradise.service;

import com.paradise.domain.entities.Location;


import java.util.List;

public interface LocationService {

    Location createLocation(Location location);
    List<Location> getAllLocations();
    Location getLocationById(Long id);
    Location deleteLocationById(Long id);
    Location updateLocation(Long id, Location entity);
}
