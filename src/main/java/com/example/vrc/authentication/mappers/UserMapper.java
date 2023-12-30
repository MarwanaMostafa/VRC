package com.example.vrc.authentication.mappers;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.models.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserDTO userDto);

    UserDTO toDto(UserEntity userEntity);
}