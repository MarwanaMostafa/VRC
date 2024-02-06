package com.example.vrc.rooms.mappers;

import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.models.RoomEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomEntity toEntity(RoomDTO roomDTO);
    RoomDTO toDto(RoomEntity roomEntity);
    @Mapping(source = "user.id", target = "ownerId")
    RoomWithoutUserDTO toRoomWithoutUserDto(RoomEntity roomEntity);
    List<RoomWithoutUserDTO> toDtoList(List<RoomEntity> rooms);
}