package com.example.vrc.rooms.controllers;

import com.example.vrc.rooms.DTOs.SharedRoomDTO;
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
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.RoomDTO;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room created successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"title\": \"title\", \"description\": \"description\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }"))),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body (like description not exist in request body)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"title\": \"title\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"title\": \"title\", \"description\": \"description\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"title\": \"title\", \"description\": \"description\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }")))
    })
    ResponseEntity<RoomWithoutUserDTO> createRoom(Authentication auth, @Valid @RequestBody RoomWithoutUserDTO roomDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();

        RoomDTO room = this.roomService.createRoom(roomDTO, userEmail);
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));
        sendRoomData(userEmail);
        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.CREATED);
    }

// TODO: Add a new endpoint to add a collaborator for a specific room. Use the sharedRoom parameter, as shown in the example endpoint below.

    @Operation(summary = API_POST_ADD_COLLABORATOR_VALUES, description = API_POST_ADD_COLLABORATOR_DESCRIPTION)
    @PostMapping("/add-collaborator")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Collaborator added successfully"),
            @ApiResponse(responseCode = "400", description = "There is an issue with the request body"),
            @ApiResponse(responseCode = "404", description = "Resource Not found"),
            @ApiResponse(responseCode = "500", description = "There is a problem on the server")
    })
    ResponseEntity<String> addCollaborator(Authentication auth, @Valid @RequestBody SharedRoomDTO sharedRoom, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);

        String userEmail = auth.getName();


        // Call a service method to add collaborator
        roomService.addCollaborator(sharedRoom.getId(), userEmail);

        // Optionally, you can return some message indicating success
        return new ResponseEntity<>("Collaborator added successfully", HttpStatus.OK);

    }


    @Operation(summary = API_PATCH_ROOMID_UPDATE_VALUES, description = API_PATCH_ROOMID_UPDATE_DESCRIPTION)
    @PatchMapping("/{roomId}/update")

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\",\"title\": \"title\", \"description\": \"description\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }"))),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body (like description not exist in request body)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"id\": \"ID\", \"title\": \"title\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\",\"title\": \"title\", \"description\": \"description\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\",\"title\": \"title\", \"description\": \"description\", \"state\": \"state\", \"isPublic\": \"booleanValue\" }")))
    })
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get all shared rooms successfully"),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body"),
            @ApiResponse(responseCode = "404", description = "Resource Not found"),
            @ApiResponse(responseCode = "500", description = "There is problem in server")
    })
    ResponseEntity<List<SharedRoomDTO>> getSharedRooms(Authentication auth) throws ResponseStatusException {
        String userEmail = auth.getName();

        List<SharedRoomDTO> sharedRoomDTOS = this.roomService.getSharedRooms(userEmail);


        return new ResponseEntity<>(sharedRoomDTOS, HttpStatus.OK);
    }


    //Why return the Room ID? Because if a user needs to update a specific room, they need to know the Room ID
    @Operation(summary = API_GET_ROOMS_VALUES, description = API_GET_ROOMS_DESCRIPTION)
    @GetMapping("/get-rooms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get all rooms successfully"),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body"),
            @ApiResponse(responseCode = "404", description = "Resource Not found"),
            @ApiResponse(responseCode = "500", description = "There is problem in server")
    })
    ResponseEntity<List<RoomWithoutUserDTO>> getRooms(Authentication auth) throws ResponseStatusException {
        String userEmail = auth.getName();

        List<RoomDTO> rooms = this.roomService.getRooms(userEmail);
        List<RoomWithoutUserDTO> roomWithoutUserDTOS = this.roomWithoutUserMapper.toDtoList(this.roomMapper.toEntities(rooms));

        return new ResponseEntity<>(roomWithoutUserDTOS, HttpStatus.OK);
    }


    @Operation(summary = API_GET_ROOM_ID_VALUES, description = API_GET_ROOM_ID_DESCRIPTION)
    @GetMapping("/get-room/{roomID}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get specific room using room id  successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\" }"))),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body (like ID not exist in request body)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ }"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\" }"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\" }")))
    })
    ResponseEntity<RoomWithoutUserDTO> getRoomByID(Authentication auth, @PathVariable UUID roomID) throws ResponseStatusException {
        String userEmail = auth.getName();

        RoomDTO room = this.roomService.getRoomByID(roomID, userEmail);

        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }

    void sendRoomData(String userEmail) {
        List<RoomDTO> rooms = roomService.getRooms(userEmail);
        List<RoomWithoutUserDTO> roomWithoutUserDTOS = roomWithoutUserMapper.toDtoList(roomMapper.toEntities(rooms));
        messagingTemplate.convertAndSend("/topic/rooms/" + userEmail, roomWithoutUserDTOS);
    }


}
