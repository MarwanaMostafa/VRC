package com.example.vrc.rooms.controllers;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.models.UserEntity;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.models.SharedRoomEntity;
import com.example.vrc.rooms.repositories.RoomRepository;
import com.example.vrc.rooms.services.RoomService;
import com.example.vrc.rooms.services.impl.RoomServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.Errors;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(MockitoJUnitRunner.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RoomService roomService = new RoomServiceImpl();

    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private Authentication authentication;
    @Mock
    private Errors errors;
    @Mock
    private UserService userService;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserWithoutPasswordMapper userWithoutPasswordMapper;

    @InjectMocks
    private RoomController roomController;

    @Autowired
    private ObjectMapper objectMapper;

    private RoomWithoutUserDTO roomWithoutUserDTO;
    private UserDTO userDTO;
    private RoomDTO roomDTO;
    private String userEmail = "test@example.com";
    private RoomEntity roomEntity;
    private String roomID = "123e4567-e89b-12d3-a456-556642440000";
    private UUID roomUUID = UUID.fromString(roomID);;
    private UserEntity userEntity;



    @WithMockUser(username = "test@example.com")
    @Test
    void createRoom_ReturnCreatedRoom() throws Exception {
        roomID="123e4567-e89b-12d3-a456-556642440000";
        roomUUID=UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        // Mock authenticated user
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        Mockito.when(authentication.getName()).thenReturn(userDetails.getUsername());
        // Mock userDTO
        String userEmail = "yousef@test.com";
        UserDTO userDTO = new UserDTO(
                11L,
                "test fName",
                "test lName",
                userEmail,
                "testPassw"
        );
        when(this.userWithoutPasswordMapper.toDto(any())).thenReturn(new UserWithoutPasswordDTO(
                15L,
                "test fName","test lName",userEmail,"testPassw"
        ));
        when(userService.getUserByEmail(userEmail)).thenReturn(userDTO);

        UserEntity userEntity = new UserEntity();
        when(this.userMapper.toEntity(any(UserDTO.class))).thenReturn(userEntity);

        // Mock room DTO
        roomWithoutUserDTO=new RoomWithoutUserDTO(
                roomUUID,
                1L,
                "Test Title",
                "Test describtion",
                "Test state",
                true

        );

        // Mock room service
        Mockito.when(roomService.createRoom(roomWithoutUserDTO, userEmail))
                .thenReturn(roomWithoutUserDTO);

        // Execute controller method
        ResponseEntity<RoomWithoutUserDTO> responseEntity = roomController.createRoom(authentication, roomWithoutUserDTO, errors);

        // Verify response
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

    }


    @Test
    void createRoom_InvalidInput_ThrowsResponseStatusException()throws Exception {

        when(authentication.getName()).thenReturn(userEmail);
        when(errors.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<RoomWithoutUserDTO> responseEntity = roomController.createRoom(authentication, roomWithoutUserDTO, errors);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void canAddCollaborator_InvalidInput() throws Exception {
        // Prepare invalid input with an invalid email address
        SharedRoomDTO sharedRoomDTO = new SharedRoomDTO(roomID, "invalid_email");

        // Perform the request and expect a 400 Bad Request
        mockMvc.perform(post("/api/rooms/add-collaborator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sharedRoomDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canSharedRoom()throws Exception {
        roomID="123e4567-e89b-12d3-a456-556642440000";
        roomUUID=UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        roomWithoutUserDTO=new RoomWithoutUserDTO(
                roomUUID,
                1L,
                "Test Title",
                "Test describtion",
                "Test state",
                true

        );
        //Mock creating room service
        when(roomService.createRoom(roomWithoutUserDTO, userEmail)).thenReturn(roomWithoutUserDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/public-room/" + roomID))
                .andExpect(status().isOk());
    }


    @Test
    void canGetRoomByID() throws Exception {
        // Given
        String roomId = "123e4567-e89b-12d3-a456-556642440000";
        UUID roomUUID = UUID.fromString(roomId);
        RoomEntity roomEntity = new RoomEntity(
                roomUUID,
                "Test Room",
                "Test Description",
                "Test State",
                true,
                new UserEntity(),
                new ArrayList<>()
        );
        //Mock RoomWithoutUserDTO
        RoomWithoutUserDTO roomWithoutUserDTO = new RoomWithoutUserDTO();
        roomWithoutUserDTO.setTitle("Test Room");
        roomWithoutUserDTO.setDescription("Test Description");
        roomWithoutUserDTO.setState("Test State");
        roomWithoutUserDTO.setIsPublic(true);

        when(roomRepository.findById(roomUUID)).thenReturn(Optional.of(roomEntity));
        when(roomMapper.toRoomWithoutUserDto(any(RoomEntity.class))).thenReturn(roomWithoutUserDTO);

        // When
        RoomWithoutUserDTO result = roomService.shareRoomById(roomId);

        // Then
        assertNotNull(result);
        assertEquals("Test Room", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Test State", result.getState());
        assertTrue(result.getIsPublic());

    }

    @Test
    void canGetRooms() {

        // Given
        userDTO = userService.getUserByEmail(userEmail);
        List<RoomEntity> rooms = new ArrayList<>();
        List<SharedRoomEntity> sharedrooms = new ArrayList<>();
        RoomEntity room1 = new RoomEntity(
                UUID.randomUUID(),
                "Room 1 Title",
                "Room 1 Description",
                "Room 1 State",
                true,
                this.userMapper.toEntity(userDTO),
                sharedrooms
        );
        rooms.add(room1);

        RoomEntity room2 = new RoomEntity(
                UUID.randomUUID(),
                "Room 2 Title",
                "Room 2 Description",
                "Room 2 State",
                false,
                this.userMapper.toEntity(userDTO),
                sharedrooms
        );
        rooms.add(room2);

        // Mock the repository
        when(roomRepository.findAllByUserEmailIgnoreCase(userEmail)).thenReturn(rooms);

        // Mock
        List<RoomWithoutUserDTO> expectedRooms = new ArrayList<>();
        // Add corresponding RoomWithoutUserDTO objects to the expected list
        expectedRooms.add(new RoomWithoutUserDTO());
        expectedRooms.add(new RoomWithoutUserDTO());
        when(this.roomMapper.toDtoList(rooms)).thenReturn(expectedRooms);

        // When
        List<RoomWithoutUserDTO> actualRooms = roomService.getRooms(userEmail);
    }

    @Test
    void getSharedRooms() {
        // Mock authenticated user
        UserDetails userDetails = new User(userEmail, "password", Collections.emptyList());
        Mockito.when(authentication.getName()).thenReturn(userDetails.getUsername());

        // Mock shared rooms
        List<RoomWithoutUserDTO> sharedRooms = new ArrayList<>();
        RoomWithoutUserDTO room1 = new RoomWithoutUserDTO(
                UUID.randomUUID(),
                1L,
                "Room 1 Title",
                "Room 1 Description",
                "Room 1 State",
                false
        );

        sharedRooms.add(room1);

        RoomWithoutUserDTO room2 = new RoomWithoutUserDTO(
                UUID.randomUUID(),
                1L,
                "Room 2 Title",
                "Room 2 Description",
                "Room 2 State",
                false
        );
        sharedRooms.add(room2);

        // Mock room service
        Mockito.when(roomService.getSharedRooms(userDetails.getUsername())).thenReturn(sharedRooms);

        // Execute controller method
        ResponseEntity<List<RoomWithoutUserDTO>> responseEntity = roomController.getSharedRooms(authentication);

        // Verify response
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateRoom() {
        // Mock authenticated user
        UserDetails userDetails = new User("test_user", "password", Collections.emptyList());
        Mockito.when(authentication.getName()).thenReturn(userDetails.getUsername());

        // Mock room DTO
        RoomWithoutUserDTO roomDTO = new RoomWithoutUserDTO(
                UUID.randomUUID(),
                1L,
                "Room 1 Title",
                "Room 1 Description",
                "Room 1 State",
                false
        );

        // Mock updated room
        RoomWithoutUserDTO updatedRoom = new RoomWithoutUserDTO(
                UUID.randomUUID(),
                1L,
                "updatedRoom Title",
                "updatedRoom Description",
                "updatedRoom State",
                false
        );


        // Mock room service
        Mockito.when(roomService.updateRoom(Mockito.anyString(), Mockito.eq(roomDTO), Mockito.anyString()))
                .thenReturn(updatedRoom);

        // Execute controller method
        ResponseEntity<RoomWithoutUserDTO> responseEntity = roomController.updateRoom(authentication, roomID, roomDTO, errors);

        // Verify response
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(updatedRoom, responseEntity.getBody());
    }

    @Test
    void canGetCollaborators(){
        // Mock authenticated user
        UserDetails userDetails = new User(userEmail, "password", Collections.emptyList());
        Mockito.when(authentication.getName()).thenReturn(userDetails.getUsername());


        // Mock collaborators
        List<String> collaborators = Arrays.asList("collaborator1@example.com", "collaborator2@example.com");

        // Mock room service
        Mockito.when(roomService.getAllCollaborator(roomID, userDetails.getUsername()))
                .thenReturn(collaborators);

        // Execute controller method
        ResponseEntity<List<String>> responseEntity = roomController.getCollaborators(authentication, roomID);

        // Verify response
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(collaborators, responseEntity.getBody());
    }

    @Test
    void canDeleteCollaborator(){
        // Mock authenticated user
        UserDetails userDetails = new User(userEmail, "password", Collections.emptyList());
        Mockito.when(authentication.getName()).thenReturn(userDetails.getUsername());

        // Mock shared room DTO
        SharedRoomDTO sharedRoomDTO = new SharedRoomDTO();
        sharedRoomDTO.setId(roomID);
        sharedRoomDTO.setCollaboratorEmail("collaborator@example.com");

        // Mock room service
        Mockito.when(roomService.deleteCollaborator(sharedRoomDTO, userDetails.getUsername()))
                .thenReturn("Collaborator successfully removed");

        // Execute controller method
        ResponseEntity<String> responseEntity = roomController.DeleteCollaborator(authentication, sharedRoomDTO, errors);

        // Verify response
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals("Collaborator successfully removed", responseEntity.getBody());
    }
}
