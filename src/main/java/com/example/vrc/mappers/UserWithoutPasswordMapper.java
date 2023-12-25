package com.example.vrc.mappers;

import com.example.vrc.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.models.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserWithoutPasswordMapper {
    UserEntity toEntity(UserWithoutPasswordDTO userWithoutPasswordDTO);

    UserWithoutPasswordDTO toDto(UserEntity userEntity);
}