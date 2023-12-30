package com.example.vrc.rooms.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public RoomDTO updateRoom(RoomWithoutUserDTO roomInfo) {
        return this.roomRepository.findById(roomInfo.getId()).map(roomEntity -> {
                roomEntity.setTitle(roomInfo.getTitle());
                roomEntity.setDescription(roomInfo.getDescription());
                roomEntity.setState(roomInfo.getState());
                roomEntity.setIsPublic(roomInfo.getIsPublic());

                return this.roomMapper.toDto(this.roomRepository.save(roomEntity));
            }).orElse(null);
    }

    @Override
    public RoomDTO saveRoom(RoomWithoutUserDTO roomInfo, String userEmail) {
        if(roomInfo.getId() != null && this.roomRepository.existsById(roomInfo.getId())) {
            return this.updateRoom(roomInfo);
        }

        return this.createRoom(roomInfo, userEmail);
    }
}
