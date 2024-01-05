package com.example.vrc.rooms.controllers;

import com.example.vrc.rooms.models.RoomEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.vrc.rooms.services.RoomService;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.mappers.RoomWithoutUserMapper;
import com.example.vrc.shared.utilities.UserInputsValidator;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomWithoutUserMapper roomWithoutUserMapper;

    @PostMapping("/create")
    ResponseEntity<RoomWithoutUserDTO> createRoom(Authentication auth, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomDTO room = this.roomService.createRoom(roomDTO, userEmail);
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{roomId}/update")
    ResponseEntity<RoomWithoutUserDTO> updateRoom(Authentication auth, @PathVariable UUID roomId, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomDTO room = this.roomService.updateRoom(roomId, roomDTO, userEmail);
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }

    //Why return the Room ID? Because if a user needs to update a specific room, they need to know the Room ID
    @GetMapping("/get-rooms")
    ResponseEntity<List<RoomWithoutUserDTO>> getRooms(Authentication auth) throws ResponseStatusException {

        String userEmail = auth.getName();

        List<RoomDTO> rooms = this.roomService.getRooms(userEmail);

        List<RoomWithoutUserDTO> roomWithoutUserDTOS = new ArrayList<>();
        for (RoomDTO i : rooms) {
            roomWithoutUserDTOS.add(this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(i)));
        }
        return new ResponseEntity<>(roomWithoutUserDTOS, HttpStatus.OK);
    }
    @GetMapping("/get-room/{roomID}")
    ResponseEntity<RoomWithoutUserDTO> getRoomByID(Authentication auth,@PathVariable UUID roomID) throws ResponseStatusException {


        RoomDTO room = this.roomService.getRoomByID(roomID);

        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }
}
