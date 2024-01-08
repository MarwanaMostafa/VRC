package com.example.vrc.rooms.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.vrc.rooms.common.documentation.DocConstant.RoomConstants.*;

import java.util.ArrayList;
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
@CrossOrigin(origins = "*", maxAge= 3600)

@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomWithoutUserMapper roomWithoutUserMapper;

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

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.CREATED);
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

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
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

        List<RoomWithoutUserDTO> roomWithoutUserDTOS = new ArrayList<>();
        for (RoomDTO i : rooms) {
            roomWithoutUserDTOS.add(this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(i)));
        }
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


        RoomDTO room = this.roomService.getRoomByID(roomID);

        RoomWithoutUserDTO roomWithoutUserDTO = this.roomWithoutUserMapper.toDto(this.roomMapper.toEntity(room));

        return new ResponseEntity<>(roomWithoutUserDTO, HttpStatus.OK);
    }
}
