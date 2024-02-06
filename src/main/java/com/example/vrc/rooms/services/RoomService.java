package com.example.vrc.rooms.services;

import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    RoomWithoutUserDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail);
    String addCollaborator(SharedRoomDTO sharedRoom,String ownerEmail);
    RoomWithoutUserDTO shareRoomById(String roomID);
    List<RoomWithoutUserDTO> getRooms(String userEmail);
    List<RoomWithoutUserDTO> getSharedRooms(String userEmail);
    RoomWithoutUserDTO getRoomByID(String roomID, String userEmail);
    RoomWithoutUserDTO updateRoom(String roomId, RoomWithoutUserDTO roomInfo, String userEmail);
    List<String> getAllCollaborator(UUID roomID);
    List<RoomWithoutUserDTO> getAllRooms(String userEmail);
    UUID convertToUUID(String ID);
    boolean isUserACollaborator(UUID roomId, String userEmail);
}
