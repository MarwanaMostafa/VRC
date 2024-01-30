package com.example.vrc.rooms.controllers;

import com.example.vrc.rooms.DTOs.*;
import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.swagger.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.vrc.rooms.common.documentation.DocConstant.RoomConstants.*;

import java.util.List;
import java.util.UUID;

import com.example.vrc.rooms.services.RoomService;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.mappers.RoomWithoutUserMapper;
import com.example.vrc.shared.utilities.UserInputsValidator;

@Tag(name = API_NAME,description = API_DESCRIPTION)
@RestController
@RequestMapping("/api/rooms")
@SecurityRequirement(name = "AddToken")

public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomWithoutUserMapper roomWithoutUserMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Operation(summary = API_POST_CREATE_VALUES, description = API_POST_CREATE_DESCRIPTION)
    @PostMapping("/create")
    @ApiFullResponseCreate
    ResponseEntity<RoomWithoutUserDTO> createRoom(Authentication auth, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomWithoutUserDTO roomWithoutUserDTO =this.roomService.createRoom(roomDTO, userEmail);

        sendRoomData(userEmail);
        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.CREATED);
    }

    @Operation(summary = API_POST_ADD_COLLABORATOR_VALUES, description = API_POST_ADD_COLLABORATOR_DESCRIPTION)
    @PostMapping("/add-collaborator")
    @ApiFullResponseAddCollaborator
    ResponseEntity<String> addCollaborator(@Valid @RequestBody SharedRoomDTO sharedRoom, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(roomService.addCollaborator(sharedRoom), HttpStatus.OK);
    }

    @Operation(summary = API_GET_ROOM_ID_VALUES, description = API_GET_ROOM_ID_DESCRIPTION)
    @GetMapping("/publicRoom/{roomID}")
    @ApiFullResponseGetSharedRoom
    ResponseEntity<RoomWithoutUserDTO> sharedRoom(@PathVariable String roomID) throws ResponseStatusException {
        return new ResponseEntity<>(this.roomService.shareRoomById(roomID), HttpStatus.OK);
    }

    @Operation(summary = API_GET_ROOMS_VALUES, description = API_GET_ROOMS_DESCRIPTION)
    @GetMapping("/get-rooms")
    @ApiFullResponseGetRooms
    ResponseEntity<List<RoomWithoutUserDTO>> getRooms(Authentication auth) throws ResponseStatusException {
        String userEmail = auth.getName();
        return new ResponseEntity<>(this.roomService.getRooms(userEmail), HttpStatus.OK);
    }






    @Operation(summary = API_PATCH_ROOMID_UPDATE_VALUES, description = API_PATCH_ROOMID_UPDATE_DESCRIPTION)
    @PatchMapping("/{roomId}/update")

    @ApiFullResponseGetRoomByID
    ResponseEntity<RoomWithoutUserDTO> updateRoom(Authentication auth, @PathVariable UUID roomId, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomDTO room = this.roomService.updateRoom(roomId, roomDTO, userEmail);
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));
        sendRoomData(userEmail);
        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }

    // TODO: Add a new endpoint to Get all Shared Rooms . as shown in the example endpoint below.
    @Operation(summary = API_GET_SHARED_ROOMS_VALUES, description = API_GET_SHARED_ROOMS_DESCRIPTION)
    @GetMapping("/get-shared-rooms")
    @ApiFullResponseGetSharedRooms
    ResponseEntity<List<SharedRoomDTO>> getSharedRooms(Authentication auth) throws ResponseStatusException {
        String userEmail = auth.getName();

        List<SharedRoomDTO> sharedRoomDTOS = this.roomService.getSharedRooms(userEmail);


        return new ResponseEntity<>(sharedRoomDTOS, HttpStatus.OK);
    }


    //Why return the Room ID? Because if a user needs to update a specific room, they need to know the Room ID



    @Operation(summary = API_GET_ROOM_ID_VALUES, description = API_GET_ROOM_ID_DESCRIPTION)
    @GetMapping("/get-room/{roomID}")
    @ApiFullResponseGetRoomByID
    ResponseEntity<RoomWithoutUserDTO> getRoomByID(Authentication auth, @PathVariable UUID roomID) throws ResponseStatusException {
        String userEmail = auth.getName();

        RoomDTO room = this.roomService.getRoomByID(roomID, userEmail);

        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }


    void sendRoomData(String userEmail) {
        List<RoomWithoutUserDTO> roomWithoutUserDTOS =  roomService.getRooms(userEmail);
        messagingTemplate.convertAndSend("/topic/rooms/" + userEmail, roomWithoutUserDTOS);
    }


}
