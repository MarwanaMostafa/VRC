package com.example.vrc.rooms.mappers;

import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.models.SharedRoomEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SharedRoomMapper {
    SharedRoomEntity toEntity(SharedRoomDTO sharedRoomDTO);

    SharedRoomDTO toDto(SharedRoomEntity sharedRoom);
}
