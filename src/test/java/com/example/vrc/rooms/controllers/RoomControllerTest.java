package com.example.vrc.rooms.controllers;

import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.services.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(RoomController.class)
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
    RoomWithoutUserDTO roomDTO = new RoomWithoutUserDTO(
            UUID.randomUUID(),
            1L,
            "Test title",
            "Test Describtion",
            "Test State",
            true
    );

    @InjectMocks
    private RoomController roomController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRoom_ReturnCreatedRoom() throws Exception {

        given(authentication.getName()).willReturn("test@example.com");
        given(roomService.createRoom(any(RoomWithoutUserDTO.class), anyString())).willReturn(roomDTO);

        ResultActions response = mockMvc.perform(post("/api/rooms/create").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(roomDTO)));
        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void createRoom_InvalidInput_ThrowsResponseStatusException() {
        // Given
        String userEmail = "user@example.com";
        when(authentication.getName()).thenReturn(userEmail);
        when(errors.hasErrors()).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> roomController.createRoom(authentication, roomDTO, errors))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void addCollaborator() {
    }

    @Test
    void sharedRoom() {
    }

    @Test
    void getRoomByID() {
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