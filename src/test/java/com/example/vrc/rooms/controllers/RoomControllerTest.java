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
import com.example.vrc.rooms.repositories.RoomRepository;
import com.example.vrc.rooms.services.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

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
    private String userEmail;
    private RoomEntity roomEntity;
    private UUID roomUUID;
    private String roomID;
    private UserEntity userEntity;

    @BeforeEach
    void init(){
        /*roomID="123e4567-e89b-12d3-a456-556642440000";
        roomUUID=UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        userEmail="test@example.com";
        when(authentication.getName()).thenReturn(userEmail);
        roomWithoutUserDTO=new RoomWithoutUserDTO(
                roomUUID,
                "Test Title",
                "Test describtion",
                "Test state",
                true

        );
        userDTO = new UserDTO(
                1L,
                "Test FName",
                "Test LName",
                userEmail,
                "Test Password"
        );
        roomEntity = new RoomEntity(
                roomUUID,
                "Test Room",
                "Test Description",
                "Test State",
                true,
                userEntity,
                new ArrayList<>()
        );
        userEntity = new UserEntity(
                1L,
                "Test FName",
                "Test LName",
                userEmail,
                "Test Password"
        );

        when(userService.getUserByEmail(anyString())).thenReturn(userDTO);
        // Mock roomMapper
        when(roomMapper.toRoomWithoutUserDto(any(RoomEntity.class))).thenReturn(roomWithoutUserDTO);
        //Mock userWithoutPasswordMapper
        when(userWithoutPasswordMapper.toDto(any(UserEntity.class))).thenReturn(new UserWithoutPasswordDTO(
                1L,
                "Test FName",
                "Test LName",
                userEmail,
                "Test Password")
        );
        //Mock userMapper
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(userEntity);
        //Mock creating room service
        when(roomService.createRoom(roomWithoutUserDTO, userEmail)).thenReturn(roomWithoutUserDTO);
        // Mock repository behavior
        when(roomRepository.findById(roomUUID)).thenReturn(Optional.of(roomEntity));*/

    }

    @Disabled
    @WithMockUser(username = "test@example.com")
    @Test
    void createRoom_ReturnCreatedRoom() throws Exception {
        // Mock room UUID
        UUID roomUUID = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");

        // Mock room DTO
        RoomWithoutUserDTO roomWithoutUserDTO = new RoomWithoutUserDTO(
                roomUUID,
                "Test Title",
                "Test description",
                "Test state",
                true
        );

        // Mock user DTO
        UserDTO userDTO = new UserDTO(
                1L,
                "Test FName",
                "Test LName",
                "test@example.com",
                "Test Password"
        );

        // Mock user entity
        UserEntity userEntity = new UserEntity(
                1L,
                "Test FName",
                "Test LName",
                "test@example.com",
                "Test Password"
        );

        // Mock RoomMapper behavior
        when(roomMapper.toRoomWithoutUserDto(any(RoomEntity.class))).thenReturn(roomWithoutUserDTO);

        // Mock UserWithoutPasswordMapper behavior
        when(userWithoutPasswordMapper.toDto(any(UserEntity.class))).thenReturn(new UserWithoutPasswordDTO(
                1L,
                "Test FName",
                "Test LName",
                "test@example.com",
                "Test Password")
        );

        // Mock UserMapper behavior
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(userEntity);

        // Mock UserService behavior
        when(userService.getUserByEmail(anyString())).thenReturn(userDTO);

        // Mock RoomService behavior
        when(roomService.createRoom(roomWithoutUserDTO, "test@example.com")).thenReturn(roomWithoutUserDTO);

        // Mock Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        // Mock RoomService behavior
        when(roomService.createRoom(roomWithoutUserDTO, "test@example.com")).thenReturn(roomWithoutUserDTO);

        // Perform the request and validate the response
        mockMvc.perform(post("/api/rooms/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomWithoutUserDTO))
                        .with(authentication(authentication))) // Pass the mocked Authentication object with the request
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(roomUUID.toString()))
                .andExpect(jsonPath("$.title").value(roomWithoutUserDTO.getTitle()))
                .andExpect(jsonPath("$.description").value(roomWithoutUserDTO.getDescription()));



    // Act
        /*ResponseEntity<RoomWithoutUserDTO> responseEntity = roomController.createRoom(authentication, roomWithoutUserDTO, errors);

        // Assert
        verify(roomService, times(1)).createRoom(roomWithoutUserDTO, userEmail);
        verifyNoMoreInteractions(roomService);
        assert responseEntity.getStatusCode() == HttpStatus.CREATED;
        assert responseEntity.getBody() == roomWithoutUserDTO;*/
    }

    @Disabled
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
                "Test Title",
                "Test describtion",
                "Test state",
                true

        );
        //Mock creating room service
        when(roomService.createRoom(roomWithoutUserDTO, userEmail)).thenReturn(roomWithoutUserDTO);
        // Perform the request and expect a 200 OK status code with the room DTO in the response body
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/publicRoom/" + roomID))
                .andExpect(status().isOk());
    }

    @Disabled
    @Test
    void canGetRoomByID() throws Exception {
        // Mock authentication
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");

        // Mock room ID and expected DTO
        String roomID = "123e4567-e89b-12d3-a456-556642440000";
        RoomWithoutUserDTO expectedDTO = new RoomWithoutUserDTO(
                UUID.fromString(roomID),
                "Test Title",
                "Test Description",
                "Test State",
                true
        );

        when(roomService.getRoomByID(eq(roomID), anyString())).thenReturn(expectedDTO);


        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/get-room/{roomID}", roomID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                /*.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(roomID))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.state").value("Test State"))
                .andExpect(jsonPath("$.isPublic").value(true))*/;

        // Verify that the service method was called with the correct arguments
        verify(roomService, times(1)).getRoomByID(eq(roomID), anyString());
        verifyNoMoreInteractions(roomService);
    }

    @Test
    void getRooms() {
    }

    @Test
    void getSharedRooms() {
    }

    @Test
    void updateRoom() {
    }

    @Test
    void sendRoomData() {
    }
}