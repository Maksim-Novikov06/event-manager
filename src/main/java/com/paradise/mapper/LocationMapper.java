package com.paradise.mapper;

import com.paradise.dto.LocationDto;
import com.paradise.domain.entities.Location;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toEntity(LocationDto dto);

    LocationDto toDto(Location location);

}
