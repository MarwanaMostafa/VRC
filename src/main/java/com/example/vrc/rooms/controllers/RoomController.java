package com.example.vrc.rooms.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.rooms.services.RoomService;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.mappers.RoomWithoutUserMapper;
import com.example.vrc.shared.utilities.UserInputsValidator;

@Controller
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomWithoutUserMapper roomWithoutUserMapper;

    @PutMapping("/save")
    ResponseEntity<RoomWithoutUserDTO> saveRoom(Authentication auth, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomDTO room = this.roomService.saveRoom(roomDTO, userEmail);
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }
}
