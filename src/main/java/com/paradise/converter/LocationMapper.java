package com.paradise.converter;

import com.paradise.dto.LocationDto;
import com.paradise.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "capacity", target = "capacity")
    @Mapping(source = "description", target = "description")
    Location toEntity(LocationDto dto);
}
