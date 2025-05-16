package com.paradise.converter;

import com.paradise.dto.LocationDto;
import com.paradise.entities.Location;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);

    Location toEntity(LocationDto dto);
}
