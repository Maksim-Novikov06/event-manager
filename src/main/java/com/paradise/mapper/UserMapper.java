package com.paradise.mapper;

import com.paradise.dto.UserDto;
import com.paradise.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserDto toDto(User user);
}
