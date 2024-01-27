package com.example.vrc.rooms.services.impl;

import com.example.vrc.rooms.repositories.SharedRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.mappers.SharedRoomMapper;
import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.models.SharedRoomEntity;
import com.example.vrc.rooms.repositories.RoomRepository;
import com.example.vrc.rooms.services.RoomService;


@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserWithoutPasswordMapper userWithoutPasswordMapper;
    @Autowired
    private SharedRoomRepository sharedRoomRepository;
    @Autowired
    private SharedRoomMapper sharedRoomMapper;

    @Override
    public RoomDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail) {
        UserDTO userDTO = userService.getUserByEmail(userEmail);

        RoomDTO roomDTO = new RoomDTO(
                null,
                roomInfo.getTitle(),
                roomInfo.getDescription(),
                roomInfo.getState(),
                roomInfo.getIsPublic(),
                this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(userDTO))
        );

        RoomEntity room = this.roomRepository.save(this.roomMapper.toEntity(roomDTO));

        return this.roomMapper.toDto(room);
    }

    @Override
    public RoomDTO updateRoom(UUID roomId, RoomWithoutUserDTO roomInfo, String userEmail) {
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomId);
        Optional<SharedRoomEntity> sharedRoomOptional = this.sharedRoomRepository.findById(roomId);
        String collaboratorEmailOptional = "";
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        if(!sharedRoomOptional.isEmpty()){
            SharedRoomEntity sharedRoom = sharedRoomOptional.get();
            collaboratorEmailOptional = sharedRoom.getCollaboratorEmail();
        }

        RoomEntity roomEntity = roomOptional.get();

        boolean isOwner = roomEntity.getUser().getEmail().equalsIgnoreCase(userEmail);
        boolean isCollaborator = roomEntity.getUser().getEmail().equalsIgnoreCase(collaboratorEmailOptional);


        if (!isOwner && !isCollaborator) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not authorized to update this room!");
        }

        roomEntity.setTitle(roomInfo.getTitle());
        roomEntity.setDescription(roomInfo.getDescription());
        roomEntity.setState(roomInfo.getState());
        roomEntity.setIsPublic(roomInfo.getIsPublic());

        return this.roomMapper.toDto(this.roomRepository.save(roomEntity));
    }

    @Override
    public List<RoomDTO> getRooms(String userEmail) {
        List<RoomEntity> rooms = roomRepository.findAllByUserEmailIgnoreCase(userEmail);

        return this.roomMapper.toDtoList(rooms);
    }

    @Override
    public RoomDTO getRoomByID(UUID roomID, String userEmail) {
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomID);

        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        RoomEntity roomEntity = roomOptional.get();

        if(!roomEntity.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not authorized to fetch this room!");
        }

        return this.roomMapper.toDto(this.roomRepository.save(roomEntity));
    }
    @Override
    public RoomDTO shareRoomById(UUID roomID) {
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomID);
        if (roomOptional.isEmpty() ||!roomOptional.get().getIsPublic()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }
        return this.roomMapper.toDto(roomOptional.get());
    }

    public boolean isUserAuthorizedForRoom(UUID roomId, String userEmail) {
        Optional<RoomEntity> room = roomRepository.findById(roomId);
        return room.isPresent() && room.get().getUser().getEmail().equalsIgnoreCase(userEmail);
    }

    @Override
    public RoomDTO addCollaborator(UUID roomID, String collaboratorEmail) {
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomID);
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }
        RoomEntity room = roomOptional.get();
        UserDTO userDTO = userService.getUserByEmail(collaboratorEmail);

        SharedRoomDTO sharedRoomDTO = new SharedRoomDTO(
                roomID,
                collaboratorEmail

        );
        return this.roomMapper.toDto(this.roomRepository.save(room));
    }

    @Override
    public List<SharedRoomDTO> getSharedRooms(String userEmail) {
        List<SharedRoomEntity> rooms = sharedRoomRepository.findAllByCollaboratorEmailIgnoreCase(userEmail);

        return this.sharedRoomMapper.toDtoList(rooms);
    }
}
