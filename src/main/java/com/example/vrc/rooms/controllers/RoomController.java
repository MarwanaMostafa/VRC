package com.example.vrc.rooms.controllers;

import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.services.RoomService;
import com.example.vrc.rooms.swagger.*;
import com.example.vrc.shared.utilities.UserInputsValidator;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;
import java.util.UUID;

import static com.example.vrc.rooms.common.documentation.DocConstant.RoomConstants.*;

@Tag(name = API_NAME,description = API_DESCRIPTION)
@RestController
@RequestMapping("/api/rooms")
@SecurityRequirement(name = "AddToken")

public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Operation(summary = API_POST_CREATE_VALUES, description = API_POST_CREATE_DESCRIPTION)
    @PostMapping("/create")
    @ApiFullResponseCreate
    ResponseEntity<RoomWithoutUserDTO> createRoom(Authentication auth, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomWithoutUserDTO roomWithoutUserDTO =this.roomService.createRoom(roomDTO, userEmail);

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.CREATED);
    }

    @Operation(summary = API_POST_ADD_COLLABORATOR_VALUES, description = API_POST_ADD_COLLABORATOR_DESCRIPTION)
    @PostMapping("/add-collaborator")
    @ApiFullResponseAddCollaborator
    ResponseEntity<String> addCollaborator(Authentication auth,@Valid @RequestBody SharedRoomDTO sharedRoom, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(roomService.addCollaborator(sharedRoom,auth.getName()), HttpStatus.OK);
    }

    @Operation(summary = API_GET_ROOM_ID_VALUES, description = API_GET_ROOM_ID_DESCRIPTION)
    @GetMapping("/public-room/{roomID}")
    @ApiFullResponseGetSharedRoom
    ResponseEntity<RoomWithoutUserDTO> sharedRoom(@PathVariable String roomID) throws ResponseStatusException {
        return new ResponseEntity<>(this.roomService.shareRoomById(roomID), HttpStatus.OK);
    }

    @Operation(summary = API_GET_ROOM_ID_VALUES, description = API_GET_ROOM_ID_DESCRIPTION)
    @GetMapping("/get-room/{roomID}")
    @ApiFullResponseGetRoomByID
    ResponseEntity<RoomWithoutUserDTO> getRoomByID(Authentication auth, @PathVariable String roomID) throws ResponseStatusException {
        String userEmail = auth.getName();

        return new ResponseEntity<>(this.roomService.getRoomByID(roomID, userEmail), HttpStatus.OK);
    }
    @Operation(summary = API_GET_ROOMS_VALUES, description = API_GET_ROOMS_DESCRIPTION)
    @GetMapping("/get-rooms")
    @ApiFullResponseGetRooms
    ResponseEntity<List<RoomWithoutUserDTO>> getRooms(Authentication auth) throws ResponseStatusException {
        String userEmail = auth.getName();
        return new ResponseEntity<>(this.roomService.getRooms(userEmail), HttpStatus.OK);
    }

    @Operation(summary = API_GET_SHARED_ROOMS_VALUES, description = API_GET_SHARED_ROOMS_DESCRIPTION)
    @GetMapping("/get-shared-rooms")
    @ApiFullResponseGetSharedRooms
    ResponseEntity<List<RoomWithoutUserDTO>> getSharedRooms(Authentication auth) throws ResponseStatusException {
        String userEmail = auth.getName();
        return new ResponseEntity<>(this.roomService.getSharedRooms(userEmail), HttpStatus.OK);
    }

    @Operation(summary = API_PATCH_ROOMID_UPDATE_VALUES, description = API_PATCH_ROOMID_UPDATE_DESCRIPTION)
    @PatchMapping("/{roomId}/update")
    @ApiFullResponseGetRoomByID
    ResponseEntity<RoomWithoutUserDTO> updateRoom(Authentication auth, @PathVariable String roomId, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        String userEmail = auth.getName();
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomService.updateRoom(roomId, roomDTO, userEmail);

        updateRoomSocket(roomDTO.getId(), roomWithoutUserDTO);
        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }

    @Operation(summary = API_GET_ALL_COLLABORATORS_VALUES, description = API_GET_ALL_COLLABORATORS_DESCRIPTION)
    @GetMapping("/get-all-collaborators/{roomID}")
    @ApiFullResponseGetAllCollaborators
    ResponseEntity<List<String>> getCollaborators(Authentication auth,@PathVariable String roomID) throws ResponseStatusException {
        String userEmail = auth.getName();
        return new ResponseEntity<>(this.roomService.getAllCollaborator(roomID,userEmail), HttpStatus.OK);
    }

    @Operation(summary = API_DELETE_REMOVE_COLLABORATOR_VALUES, description = API_DELETE_REMOVE_COLLABORATOR_DESCRIPTION)
    @DeleteMapping("/delete-collaborator")
    @ApiFullResponseDeleteCollaborator
    ResponseEntity<String> DeleteCollaborator(Authentication auth,@Valid @RequestBody SharedRoomDTO sharedRoom, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(roomService.deleteCollaborator(sharedRoom,auth.getName()), HttpStatus.OK);
    }
    void updateRoomSocket(UUID roomId, RoomWithoutUserDTO roomData){
        System.out.println("Room id is " + roomId + roomData);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomData.getId(), roomData);

    }

}
