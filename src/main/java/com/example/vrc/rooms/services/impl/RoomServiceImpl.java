package com.example.vrc.rooms.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.models.RoomEntity;
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

        if(roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        RoomEntity roomEntity = roomOptional.get();

        if(!roomEntity.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not authorized to update this room!");
        }

        roomEntity.setTitle(roomInfo.getTitle());
        roomEntity.setDescription(roomInfo.getDescription());
        roomEntity.setState(roomInfo.getState());
        roomEntity.setIsPublic(roomInfo.getIsPublic());

        return this.roomMapper.toDto(this.roomRepository.save(roomEntity));
    }
}
