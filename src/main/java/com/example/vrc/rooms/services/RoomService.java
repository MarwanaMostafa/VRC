package com.example.vrc.rooms.services;

import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;

public interface RoomService {
    RoomDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail);
    RoomDTO updateRoom(RoomWithoutUserDTO roomInfo);
    RoomDTO saveRoom(RoomWithoutUserDTO roomInfo, String userEmail);
}
