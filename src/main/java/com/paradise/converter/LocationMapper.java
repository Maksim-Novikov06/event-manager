package com.paradise.converter;

import com.paradise.dto.LocationDto;
import com.paradise.entities.Location;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toEntity(LocationDto dto);

    LocationDto toDto(Location location);

}
