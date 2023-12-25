package com.example.vrc.mappers;

import com.example.vrc.DTOs.UserDTO;
import com.example.vrc.models.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserDTO userDto);

    UserDTO toDto(UserEntity userEntity);
}