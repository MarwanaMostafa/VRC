package com.example.vrc.rooms.services.impl;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.models.SharedRoomEntity;
import com.example.vrc.rooms.repositories.RoomRepository;
import com.example.vrc.rooms.repositories.SharedRoomRepository;
import com.example.vrc.rooms.services.RoomService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserWithoutPasswordMapper userWithoutPasswordMapper;
    @Autowired
    private SharedRoomRepository sharedRoomRepository;

    @Autowired
    @Lazy
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public RoomWithoutUserDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail) {
        UserDTO userDTO = userService.getUserByEmail(userEmail);
        log.info("Fetching user who needs to create a room");
        if(userDTO==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }
        RoomDTO roomDTO = new RoomDTO(
                null,
                roomInfo.getTitle(),
                roomInfo.getDescription(),
                roomInfo.getState(),
                roomInfo.getIsPublic(),
                this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(userDTO))
        );

        log.info("Creating Room DTO with the following fields: {}", roomDTO.toString());

        RoomEntity room = this.roomRepository.save(this.roomMapper.toEntity(roomDTO));

        log.info("Create Room Success ");
        return this.roomMapper.toRoomWithoutUserDto(room);
    }

    @Override
    public String addCollaborator(SharedRoomDTO sharedRoomDTO,String ownerEmail) {

        String collaboratorEmail=sharedRoomDTO.getCollaboratorEmail();
        UUID ID =convertToUUID(sharedRoomDTO.getId());
        log.info("Collaborate Email is {} and UUID is {}",collaboratorEmail,ID);

        Optional<RoomEntity> roomOptional = Optional.ofNullable(this.roomRepository.findByUserEmailIgnoreCaseAndId(ownerEmail, ID));
        log.info("Check If Room ID is Exist {}", roomOptional.isPresent());
        UserDTO userDTO = userService.getUserByEmail(collaboratorEmail);
        log.info("We Get User DTO To Check If this Collaborator email exist or not");
        //Check if this room exist
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no room with the entered ID that belongs to you!");
        }

        //Check if that user exist
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }

        RoomEntity room = roomOptional.get();

        log.info("Check List Collaborators For Room ID to know if user already added or not ");
        //Check if this room is Shared with this collaborator or no
        for (SharedRoomEntity SharedRoom : room.getSharedRooms()) {
            if (SharedRoom.getCollaborator().equals(userDTO.getEmail()))
                return "This user already added in this room before";
        }

        SharedRoomEntity sharedRoom = new SharedRoomEntity(userDTO.getEmail(),room);
        room.addCollaborator(sharedRoom);

        //save in DB
        this.sharedRoomRepository.save(sharedRoom);
        notifyAddedCollaborators(userDTO.getEmail(), room);
        return "User added to the room successfully";
    }

    public void notifyAddedCollaborators(String name, RoomEntity room){
    RoomWithoutUserDTO roomWithoutUserDTO = this.roomMapper.toRoomWithoutUserDto(room);
        messagingTemplate.convertAndSendToUser(name, "/topic/added", roomWithoutUserDTO);
    }

    @Override
    public String deleteCollaborator(SharedRoomDTO sharedRoomDTO, String ownerEmail) {
        String collaboratorEmail = sharedRoomDTO.getCollaboratorEmail();
        UUID ID = convertToUUID(sharedRoomDTO.getId());
        log.info("Collaborate Email is {} and UUID is {}", collaboratorEmail, ID);

        Optional<RoomEntity> roomOptional = Optional.ofNullable(this.roomRepository.findByUserEmailIgnoreCaseAndId(ownerEmail, ID));
        log.info("Check If Room ID is Exist {}", roomOptional.isPresent());
        UserDTO userDTO = userService.getUserByEmail(collaboratorEmail);
        log.info("We Get User DTO To Check If this Collaborator email exist or not");
        //Check if this room exist
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no room with the entered ID that belongs to you!");
        }

        //Check if that user exist
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }

        RoomEntity room = roomOptional.get();

        log.info("Check List Collaborators For Room ID to know if user already belong to or not ");
        //Check if this room is Shared with this collaborator or no
        for (SharedRoomEntity SharedRoom : room.getSharedRooms()) {
            if (SharedRoom.getCollaborator().equals(userDTO.getEmail())) {
                    this.sharedRoomRepository.delete(SharedRoom);
                    notifyRemovedCollaborators(userDTO.getEmail(), room);
                    return "Delete Successful.";
            }
        }

        return userDTO.getEmail()+"Not belong to this room.";
    }

    public void notifyRemovedCollaborators(String name, RoomEntity room){
        RoomWithoutUserDTO roomWithoutUserDTO = this.roomMapper.toRoomWithoutUserDto(room);
        messagingTemplate.convertAndSendToUser(name, "/topic/removed", roomWithoutUserDTO);
    }
    @Override
    @Cacheable(value = "roomCache", key = "#ID")
    public RoomWithoutUserDTO shareRoomById(String ID) {
        UUID roomID=convertToUUID(ID);
        log.info("User Enter Right Room ID {}",roomID);

        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomID);
        if (roomOptional.isEmpty() ||!roomOptional.get().getIsPublic()) {
            log.info("This ID isn't exist in DB or Is Not Public {}",roomID);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        return this.roomMapper.toRoomWithoutUserDto(roomOptional.get());
    }

    @Override
    public List<RoomWithoutUserDTO> getRooms(String userEmail) {
        List<RoomEntity> rooms = roomRepository.findAllByUserEmailIgnoreCase(userEmail);
        return this.roomMapper.toDtoList(rooms);
    }

    @Override
    public List<RoomWithoutUserDTO> getSharedRooms(String userEmail) {
        List<SharedRoomEntity> rooms = sharedRoomRepository.findAllByCollaboratorIgnoreCase(userEmail);
        List<RoomEntity>roomEntities=new ArrayList<>();
        log.info("User {} is collaborating on {} shared rooms", userEmail, rooms.size());
        for(SharedRoomEntity sharedRoom: rooms)
            roomEntities.add(sharedRoom.getRoom());

        return this.roomMapper.toDtoList(roomEntities);
    }

    @Override
    @Cacheable(value = "privateRoomCache", key = "#ID + #userEmail")
    public RoomWithoutUserDTO getRoomByID(String ID, String userEmail) {
        RoomEntity roomEntity=getRoomEntityByID(ID,userEmail);
        return this.roomMapper.toRoomWithoutUserDto(roomEntity);
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "roomCache", key = "#roomId"),
                    @CachePut(value = "privateRoomCache", key = "#roomId + #userEmail")
            }
    )
    public RoomWithoutUserDTO updateRoom(String roomId, RoomWithoutUserDTO roomInfo, String userEmail) {
        log.info("Start Updating");
        RoomEntity roomEntity = getRoomEntityByID(roomId, userEmail);
        log.info("User enter valid Room ID : "+roomId);

        log.info("User Logged In IS : "+roomEntity.getUser().getEmail());
        roomEntity.setDescription(roomInfo.getDescription());
        roomEntity.setState(roomInfo.getState());
        roomEntity.setIsPublic(roomInfo.getIsPublic());
        roomEntity.setTitle(roomInfo.getTitle());
        log.info("Update Room Success which " +
                "Description is : "+roomEntity.getDescription()+
                "State is : "+roomEntity.getState()+
                "IsPublic is : "+roomEntity.getIsPublic()+
                "Title is : "+roomEntity.getTitle());
        return this.roomMapper.toRoomWithoutUserDto(this.roomRepository.save(roomEntity));
    }

    @Override
    public List<String> getAllCollaborator(UUID roomID) {
        List<String> allUsers = new ArrayList<>();

        Optional<RoomEntity> roomOptional = roomRepository.findById(roomID);
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }
        allUsers.add(roomOptional.get().getUser().getEmail());

        List<SharedRoomEntity> sharedRoomsOptional = sharedRoomRepository.findByRoom_Id(roomID);

        for (SharedRoomEntity sharedRoom : sharedRoomsOptional)
            allUsers.add(sharedRoom.getCollaborator());

        //Logger
        for (String str : allUsers)
            log.info("Collaborator in this room is : " + str);

        return allUsers;
    }

    @Override
    public List<String> getAllCollaborator(String ID,String email) {
        List<String> allUsers = new ArrayList<>();
        UUID roomID=convertToUUID(ID);
        Optional<RoomEntity> roomOptional = Optional.ofNullable(roomRepository.findByUserEmailIgnoreCaseAndId(email, roomID));

        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "There is no room with the entered ID that belongs to you!");
        }

        List<SharedRoomEntity> sharedRoomsOptional = sharedRoomRepository.findByRoom_Id(roomID);

        for (SharedRoomEntity sharedRoom : sharedRoomsOptional)
            allUsers.add(sharedRoom.getCollaborator());

        //Logger
        for (String str : allUsers)
            log.info("Collaborator in this room is : " + str);

        return allUsers;
    }

    @Override
    public List<RoomWithoutUserDTO> getAllRooms(String userEmail) {
        List<RoomEntity> rooms = roomRepository.findAllByUserEmailIgnoreCase(userEmail);

        List<SharedRoomEntity> sharedRooms = sharedRoomRepository.findAllByCollaboratorIgnoreCase(userEmail);

        List<RoomWithoutUserDTO> allRooms = this.roomMapper.toDtoList(rooms);

        // Map shared rooms to DTOs
        for (SharedRoomEntity sharedRoom : sharedRooms)
            allRooms.add(this.roomMapper.toRoomWithoutUserDto(sharedRoom.getRoom()));

        return allRooms;
    }
    private RoomEntity getRoomEntityByID(String ID, String userEmail){
        UUID roomID=convertToUUID(ID);
        log.info("Room ID is in right format " + roomID);

        Optional<RoomEntity> roomOptional = Optional.ofNullable(this.roomRepository.findByUserEmailIgnoreCaseAndId(userEmail, roomID));
        log.info("Room Option is null ? " + roomOptional.isEmpty());

        Optional<SharedRoomEntity>sharedRoomEntityOptional= Optional.ofNullable(this.sharedRoomRepository.findByRoom_IdAndAndCollaboratorIgnoreCase(roomID, userEmail));
        log.info("SharedRoomEntity Option is null ? " + sharedRoomEntityOptional.isEmpty());

        if (roomOptional.isEmpty() && sharedRoomEntityOptional.isEmpty()) {
            log.info("There is not room for user and this user not Collaborate for this room " + roomID);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        if(roomOptional.isEmpty())
            return (sharedRoomEntityOptional.get().getRoom());

        log.info("sharedRoomEntityOptional is already Null");
        return (roomOptional.get());
    }

    @Override
    public UUID convertToUUID(String ID) {
        try {
            return UUID.fromString(ID);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

    }

    @Transactional
    public boolean isUserACollaborator(UUID roomId, String userEmail) {
        List<String> collaborators = this.getAllCollaborator(roomId);
        return collaborators.contains(userEmail);

    }
}
