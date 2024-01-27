package com.example.vrc.rooms.services;

import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface RoomService {
    RoomDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail);
    RoomDTO updateRoom(UUID roomId, RoomWithoutUserDTO roomInfo, String userEmail);

    List<RoomDTO> getRooms(String userEmail);
    RoomDTO getRoomByID(UUID roomID, String userEmail);

    boolean isUserAuthorizedForRoom(UUID roomId, String userEmail);

    void addCollaborator(UUID roomID, String collaboratorEmail);

    List<SharedRoomDTO> getSharedRooms(String userEmail);
}
