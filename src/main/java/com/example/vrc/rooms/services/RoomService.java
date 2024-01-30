package com.example.vrc.rooms.services;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    RoomWithoutUserDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail);
    String addCollaborator(String roomID, String collaboratorEmail);


    RoomDTO updateRoom(UUID roomId, RoomWithoutUserDTO roomInfo, String userEmail);

    List<RoomDTO> getRooms(String userEmail);
    RoomDTO getRoomByID(UUID roomID, String userEmail);
    RoomDTO shareRoomById(UUID roomID);

    boolean isUserAuthorizedForRoom(UUID roomId, String userEmail);


    List<SharedRoomDTO> getSharedRooms(String userEmail);

    List<UserDTO>getAllCollaborator(UUID roomID);

    List<RoomDTO> getAllRooms(String userEmail);
}
