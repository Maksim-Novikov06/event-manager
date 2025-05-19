package com.paradise.converter;

import com.paradise.dto.UserDto;
import com.paradise.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserDto toDto(User user);
}
