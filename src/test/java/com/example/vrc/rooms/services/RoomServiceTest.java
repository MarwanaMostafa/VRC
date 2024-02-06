package com.example.vrc.rooms.services;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.models.UserEntity;
import com.example.vrc.authentication.repositories.UserRepository;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.mappers.SharedRoomMapper;
import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.models.SharedRoomEntity;
import com.example.vrc.rooms.repositories.RoomRepository;
import com.example.vrc.rooms.repositories.SharedRoomRepository;
import com.example.vrc.rooms.services.impl.RoomServiceImpl;
import org.apache.coyote.BadRequestException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private SharedRoomRepository sharedRoomRepository;
    @InjectMocks
    private RoomService roomService = new RoomServiceImpl();

    @Mock
    private RoomMapper roomMapper;
    @Mock
    private UserWithoutPasswordMapper userWithoutPasswordMapper;


    @Test
    void createRoomWithWrongEmail() {
        String userEmail = "yousef@test.com";
        RoomWithoutUserDTO roomInfo = new RoomWithoutUserDTO();
        roomInfo.setTitle("Test Room");
        roomInfo.setDescription("Test Description");
        roomInfo.setState("Test State");
        roomInfo.setIsPublic(true);

        RoomEntity savedRoomEntity = new RoomEntity();
        savedRoomEntity.setTitle(roomInfo.getTitle());
        savedRoomEntity.setDescription(roomInfo.getDescription());
        savedRoomEntity.setState(roomInfo.getState());
        savedRoomEntity.setIsPublic(roomInfo.getIsPublic());

        // Mocking
        when(userService.getUserByEmail(userEmail)).thenReturn(null);

        // When and then
        assertThatThrownBy(() -> roomService.createRoom(roomInfo, userEmail))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageContaining("There's no user with the entered email!");

    }

    @Test
    void createRoomWithCorrectEmail() {
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

        RoomWithoutUserDTO roomInfo = new RoomWithoutUserDTO();
        roomInfo.setTitle("Test Room");
        roomInfo.setDescription("Test Description");
        roomInfo.setState("Test State");
        roomInfo.setIsPublic(true);

        // When
        RoomWithoutUserDTO createdRoom = roomService.createRoom(roomInfo, userEmail);

        // Then
        ArgumentCaptor<RoomEntity> roomEntityArgumentCaptor =
                ArgumentCaptor.forClass(RoomEntity.class);

        verify(roomRepository)
                .save(roomEntityArgumentCaptor.capture());

        RoomEntity capturedRoom = roomEntityArgumentCaptor.getValue();
        assertThat(capturedRoom).isEqualTo(createdRoom);

    }

    @Test
    void canAddExistUserToExistedRoom() {
        // Given
        String roomId = "123e4567-e89b-12d3-a456-556642440000";
        String collaboratorEmail = "collaborator@test.com";
        SharedRoomDTO sharedRoomDTO = new SharedRoomDTO(collaboratorEmail, roomId);


        UserDTO userDTO = new UserDTO(
                12L,
                "Collaborator",
                "Lastname",
                collaboratorEmail,
                "testPassw"
        );
        when(userService.getUserByEmail(collaboratorEmail)).thenReturn(userDTO);


        UUID roomUUID = UUID.fromString(roomId);
        RoomEntity roomEntity = new RoomEntity();
        when(roomRepository.findById(roomUUID)).thenReturn(Optional.of(roomEntity));


        // When
        String result = roomService.addCollaborator(sharedRoomDTO, "email@email.com");

        // Then
        assertNotNull(result);
        assertEquals("User added to the room successfully", result);
    }


    @Test
    void addWrongCollaboratorToExistedRoom(){
        // Given
        String roomId = "123e4567-e89b-12d3-a456-556642440000"; // Wrong Room Id
        String collaboratorEmail = "collaborator@test.com";
        UserDTO userDTO = new UserDTO(
                12L,
                "Collaborator",
                "Lastname",
                collaboratorEmail,
                "testPassw"
        );
        when(userService.getUserByEmail(collaboratorEmail)).thenReturn(userDTO);

        // Mock the shared room DTO
        SharedRoomDTO sharedRoomDTO = new SharedRoomDTO(collaboratorEmail, roomId);

        // When and then
        assertThatThrownBy(() -> roomService.addCollaborator(sharedRoomDTO,"email@email.com"))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageContaining("There's no room with the entered id!");
    }


    @Test
    void canShareRoomById() {
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
        String userEmail = "user@example.com";
        UserDTO userDTO = userService.getUserByEmail(userEmail);
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

        // Then
        assertThat(actualRooms).isEqualTo(expectedRooms);
    }

    @Test
    void canGetSharedRooms() {
        // Given
        String userEmail = "user@example.com";
        List<SharedRoomEntity> sharedRooms = new ArrayList<>();

        SharedRoomEntity sharedRoom1 = new SharedRoomEntity(1L, userEmail, new RoomEntity());
        sharedRooms.add(sharedRoom1);
        SharedRoomEntity sharedRoom2 = new SharedRoomEntity(2L, userEmail, new RoomEntity());
        sharedRooms.add(sharedRoom2);

        // Mock the repository
        when(sharedRoomRepository.findAllByCollaboratorIgnoreCase(userEmail)).thenReturn(sharedRooms);


        List<RoomWithoutUserDTO> expectedSharedRooms = new ArrayList<>();

        expectedSharedRooms.add(new RoomWithoutUserDTO());
        expectedSharedRooms.add(new RoomWithoutUserDTO());
        when(roomMapper.toDtoList(anyList())).thenReturn(expectedSharedRooms);

        // When
        List<RoomWithoutUserDTO> actualSharedRooms = roomService.getSharedRooms(userEmail);

        // Then
        assertThat(actualSharedRooms).isEqualTo(expectedSharedRooms);
    }


    @Test
    void canGetRoomById() {
        // Given
        String roomId = "123e4567-e89b-12d3-a456-556642440000";
        String userEmail = "user@example.com";
        UUID roomUUID = UUID.fromString(roomId);

        // Mock RoomEntity
        RoomEntity roomEntity = new RoomEntity(
                roomUUID,
                "Room Title",
                "Room Description",
                "Room State",
                true,
                new UserEntity(),
                new ArrayList<>()
        );

        //Mock Shared room Entity
        SharedRoomEntity sharedRoomEntity = new SharedRoomEntity();

        // Mock RoomWithoutUserDTO
        RoomWithoutUserDTO roomWithoutUserDTO = new RoomWithoutUserDTO();
        roomWithoutUserDTO.setTitle("Test Room");
        roomWithoutUserDTO.setDescription("Test Description");
        roomWithoutUserDTO.setState("Test State");
        roomWithoutUserDTO.setIsPublic(true);

        //Mock UserDTO
        String collaboratorEmail = "user@test.com";
        UserDTO userDTO = new UserDTO(
                12L,
                "Collaborator",
                "Lastname",
                collaboratorEmail,
                "testPassw"
        );

        // Mock roomMapper
        when(roomMapper.toRoomWithoutUserDto(roomEntity)).thenReturn(roomWithoutUserDTO);


        when(roomRepository.findByUserEmailIgnoreCaseAndId(userEmail, roomUUID)).thenReturn(roomEntity);

        when(sharedRoomRepository.findByRoom_IdAndAndCollaboratorIgnoreCase(roomUUID, userEmail)).thenReturn(sharedRoomEntity);



        // When
        RoomWithoutUserDTO result = roomService.getRoomByID(roomId, userEmail);


        // Then
        assertNotNull(result);
        assertEquals("Test Room", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Test State", result.getState());
        assertTrue(result.getIsPublic());
    }


    @Test
    void canUpdateRoom() {
        // Given
        String roomId = "123e4567-e89b-12d3-a456-556642440000";
        String userEmail = "user@example.com";
        UUID roomUUID = UUID.fromString(roomId);

        //Mock Shared room Entity
        SharedRoomEntity sharedRoomEntity = new SharedRoomEntity();

        // Mock userDTO
        UserDTO userDTO = new UserDTO(
                1L,
                "User",
                "Example",
                userEmail,
                "password"
        );

        // Mock RoomEntity
        RoomEntity roomEntity = new RoomEntity(
                roomUUID,
                "Room Title",
                "Room Description",
                "Room State",
                true,
                new UserEntity(),
                new ArrayList<>()
        );

        //Mock RoomWithoutUserDTO
        RoomWithoutUserDTO roomWithoutUserDTO = new RoomWithoutUserDTO();
        roomWithoutUserDTO.setTitle("New Room Title");
        roomWithoutUserDTO.setDescription("New Room Description");
        roomWithoutUserDTO.setState("New Room State");
        roomWithoutUserDTO.setIsPublic(true);

        // Mock roomMapper
        when(roomMapper.toRoomWithoutUserDto(roomEntity)).thenReturn(roomWithoutUserDTO);

        when(roomRepository.findByUserEmailIgnoreCaseAndId(userEmail, roomUUID)).thenReturn(roomEntity);

        when(sharedRoomRepository.findByRoom_IdAndAndCollaboratorIgnoreCase(roomUUID, userEmail)).thenReturn(sharedRoomEntity);

        when(roomRepository.save(roomEntity)).thenReturn(roomEntity);

        // When
        RoomWithoutUserDTO updatedRoom = roomService.updateRoom(roomId, roomWithoutUserDTO, userEmail);

        // Then
        assertNotNull(updatedRoom);
        assertEquals("New Room Title", updatedRoom.getTitle());
        assertEquals("New Room Description", updatedRoom.getDescription());
        assertEquals("New Room State", updatedRoom.getState());
        assertTrue(updatedRoom.getIsPublic());
    }


    @Test
    void canGetAllCollaborators() {
        // Given
        UUID roomId = UUID.randomUUID();

        // Mock room entity
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        roomEntity.setUser(new UserEntity(
                16L,
                "FName",
                "LName",
                "user@example.com",
                "Test Password"));
        // Mock shared room entities
        List<SharedRoomEntity> sharedRoomEntities = new ArrayList<>();
        SharedRoomEntity sharedRoom1 = new SharedRoomEntity(
                1L,
                "collaborator1@example.com",
                roomEntity);
        SharedRoomEntity sharedRoom2 = new SharedRoomEntity(
                2L,
                "collaborator2@example.com",
                roomEntity);
        sharedRoomEntities.add(sharedRoom1);
        sharedRoomEntities.add(sharedRoom2);

        // Mock roomRepository findById operation
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(roomEntity));

        // Mock sharedRoomRepository findByRoom_Id operation
        when(sharedRoomRepository.findByRoom_Id(roomId)).thenReturn(sharedRoomEntities);

        // When
        List<String> actualCollaborators = roomService.getAllCollaborator(roomId);

        // Then
        List<String> expectedCollaborators = Arrays.asList("user@example.com", "collaborator1@example.com", "collaborator2@example.com");
        assertThat(actualCollaborators).containsExactlyInAnyOrderElementsOf(expectedCollaborators);
    }


    @Test
    void canGetAllRooms() {
        // Given
        String userEmail = "user@example.com";

        // Mock rooms
        List<RoomEntity> rooms = new ArrayList<>();
        RoomEntity room1 = new RoomEntity(
                UUID.randomUUID(),
                "Room 1 Title",
                "Room 1 Description",
                "Room 1 State",
                true,
                new UserEntity(),
                new ArrayList<>()
        );
        RoomEntity room2 = new RoomEntity(
                UUID.randomUUID(),
                "Room 2 Title",
                "Room 2 Description",
                "Room 2 State",
                true,
                new UserEntity(),
                new ArrayList<>()
        );
        rooms.add(room1);
        rooms.add(room2);
        when(roomRepository.findAllByUserEmailIgnoreCase(userEmail)).thenReturn(rooms);

        // Mock shared rooms
        List<SharedRoomEntity> sharedRooms = new ArrayList<>();
        SharedRoomEntity sharedRoom1 = new SharedRoomEntity(1L, userEmail, room1);
        SharedRoomEntity sharedRoom2 = new SharedRoomEntity(2L, userEmail, room2);
        sharedRooms.add(sharedRoom1);
        sharedRooms.add(sharedRoom2);
        when(sharedRoomRepository.findAllByCollaboratorIgnoreCase(userEmail)).thenReturn(sharedRooms);

        // Mock
        List<RoomWithoutUserDTO> expectedRooms = new ArrayList<>();
        expectedRooms.add(new RoomWithoutUserDTO());
        expectedRooms.add(new RoomWithoutUserDTO());
        when(roomMapper.toDtoList(rooms)).thenReturn(expectedRooms);

        // Mock
        List<RoomWithoutUserDTO> expectedSharedRooms = new ArrayList<>();
        expectedSharedRooms.add(new RoomWithoutUserDTO());
        expectedSharedRooms.add(new RoomWithoutUserDTO());
        when(roomMapper.toRoomWithoutUserDto(any(RoomEntity.class))).thenReturn(new RoomWithoutUserDTO());

        // When
        List<RoomWithoutUserDTO> actualRooms = roomService.getAllRooms(userEmail);

        // Then
        assertThat(actualRooms).containsExactlyInAnyOrderElementsOf(expectedRooms);
    }

    @Test
    void isUserAuthorizedForRoomReturnTure() {
        // Given
        UUID roomId = UUID.randomUUID();
        String userEmail = "user@example.com";

        // Mock room entity
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setUser(new UserEntity(
                1L,
                "Test FName",
                "Test LName",
                userEmail,
                "password")
        );

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(roomEntity));

        // When
        boolean isAuthorized = roomService.isUserACollaborator(roomId, userEmail);

        // Then
        assertTrue(isAuthorized);
    }
}