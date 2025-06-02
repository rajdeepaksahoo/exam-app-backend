package com.online.exam.mappers;

import com.online.exam.dto.UserDto;
import com.online.exam.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserModel user);

    UserModel toEntity(UserDto userDto);
}

