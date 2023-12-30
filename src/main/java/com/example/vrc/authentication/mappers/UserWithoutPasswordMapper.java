package com.example.vrc.authentication.mappers;

import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.models.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserWithoutPasswordMapper {
    UserEntity toEntity(UserWithoutPasswordDTO userWithoutPasswordDTO);

    UserWithoutPasswordDTO toDto(UserEntity userEntity);
}