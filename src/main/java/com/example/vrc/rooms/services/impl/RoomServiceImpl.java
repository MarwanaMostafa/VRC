package com.example.vrc.rooms.services.impl;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.rooms.DTOs.RoomDTO;
import com.example.vrc.rooms.DTOs.RoomWithoutUserDTO;
import com.example.vrc.rooms.DTOs.SharedRoomDTO;
import com.example.vrc.rooms.mappers.RoomMapper;
import com.example.vrc.rooms.mappers.SharedRoomMapper;
import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.models.SharedRoomEntity;
import com.example.vrc.rooms.repositories.RoomRepository;
import com.example.vrc.rooms.repositories.SharedRoomRepository;
import com.example.vrc.rooms.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
    private SharedRoomMapper sharedRoomMapper;

    @Override
    public RoomWithoutUserDTO createRoom(RoomWithoutUserDTO roomInfo, String userEmail) {
        UserDTO userDTO = userService.getUserByEmail(userEmail);

        RoomDTO roomDTO = new RoomDTO(
                null,
                roomInfo.getTitle(),
                roomInfo.getDescription(),
                roomInfo.getState(),
                roomInfo.getIsPublic(),
                this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(userDTO))
        );

        RoomEntity room = this.roomRepository.save(this.roomMapper.toEntity(roomDTO));
        return this.roomMapper.toRoomWithoutUserDto(room);
    }

    @Override
    public String addCollaborator(SharedRoomDTO sharedRoomDTO) {

        String collaboratorEmail=sharedRoomDTO.getCollaboratorEmail();
        UUID ID =convertToUUID(sharedRoomDTO.getId());
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(ID);
        UserDTO userDTO = userService.getUserByEmail(collaboratorEmail);

        //Check if this room exist
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        //Check if that user exist
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }

        RoomEntity room = roomOptional.get();

        //Check if this room is Shared with this collaborator or no
        for (SharedRoomEntity SharedRoom : room.getSharedRooms()) {
            if (SharedRoom.getCollaborator().equals(collaboratorEmail))
                return "This user already added in this room before";
        }

        SharedRoomEntity sharedRoom = new SharedRoomEntity(collaboratorEmail,room);
        room.addCollaborator(sharedRoom);

        //save in DB
        this.sharedRoomRepository.save(sharedRoom);
        return "User added to the room successfully";
    }

    @Override
    public RoomWithoutUserDTO shareRoomById(String ID) {
        UUID roomID=convertToUUID(ID);
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomID);
        if (roomOptional.isEmpty() ||!roomOptional.get().getIsPublic()) {
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

        for(SharedRoomEntity sharedRoom: rooms)
            roomEntities.add(sharedRoom.getRoom());

        return this.roomMapper.toDtoList(roomEntities);
    }
    public UUID convertToUUID(String ID)
    {
        try {
            return UUID.fromString(ID);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

    }
    @Override
    public RoomDTO updateRoom(UUID roomId, RoomWithoutUserDTO roomInfo, String userEmail) {
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomId);
        Optional<SharedRoomEntity> sharedRoomOptional = this.sharedRoomRepository.findById(0L);
        List<String> collaboratorEmailOptional = new ArrayList<>();
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        if(!sharedRoomOptional.isEmpty()){
            SharedRoomEntity sharedRoom = sharedRoomOptional.get();
//            collaboratorEmailOptional = sharedRoom.getCollaborator();
        }

        RoomEntity roomEntity = roomOptional.get();

        boolean isOwner = roomEntity.getUser().getEmail().equalsIgnoreCase(userEmail);
        boolean isCollaborator =false;
        //roomEntity.getUser().getEmail().equalsIgnoreCase(collaboratorEmailOptional);
        for(String collaboratorEmail:collaboratorEmailOptional){
            if(roomEntity.getUser().getEmail().equalsIgnoreCase(collaboratorEmail)){
                isCollaborator=true;
                break;
            }
        }

        if (!isOwner && !isCollaborator) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not authorized to update this room!");
        }

        roomEntity.setTitle(roomInfo.getTitle());
        roomEntity.setDescription(roomInfo.getDescription());
        roomEntity.setState(roomInfo.getState());
        roomEntity.setIsPublic(roomInfo.getIsPublic());

        return this.roomMapper.toDto(this.roomRepository.save(roomEntity));
    }


    @Override
    public RoomDTO getRoomByID(UUID roomID, String userEmail) {
        Optional<RoomEntity> roomOptional = this.roomRepository.findById(roomID);

        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }

        RoomEntity roomEntity = roomOptional.get();

        if(!roomEntity.getUser().getEmail().equalsIgnoreCase(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not authorized to fetch this room!");
        }

        return this.roomMapper.toDto(this.roomRepository.save(roomEntity));
    }


    public boolean isUserAuthorizedForRoom(UUID roomId, String userEmail) {
        Optional<RoomEntity> room = roomRepository.findById(roomId);
        return room.isPresent() && room.get().getUser().getEmail().equalsIgnoreCase(userEmail);
    }

    @Override
    public List<RoomDTO> getAllRooms(String userEmail) {
        List<RoomEntity> rooms = roomRepository.findAllByUserEmailIgnoreCase(userEmail);
//        List<SharedRoomEntity> sharedRooms = sharedRoomRepository.findAllByCollaboratorEmailIgnoreCase(userEmail);
        List<RoomDTO> allRooms = new ArrayList<>();

        // Map user rooms to DTOs
//        allRooms.addAll(roomMapper.toDtoList(rooms));

        // Map shared rooms to DTOs
//        for (SharedRoomEntity sharedRoom : sharedRooms) {
//            allRooms.add(roomMapper.toDto(sharedRoom.getRoom()));
//        }

        return allRooms;
    }

    @Override
    public List<UserDTO> getAllCollaborator(UUID roomID) {
        Optional<RoomEntity> roomOptional = roomRepository.findById(roomID);
        if (roomOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no room with the entered id!");
        }
        RoomEntity room = roomOptional.get();

        //get OwnerDTO
        String ownerEmail = room.getUser().getEmail();
        UserDTO ownerDTO = userService.getUserByEmail(ownerEmail);
        List<UserDTO> collaborators = new ArrayList<>();
        collaborators.add(ownerDTO);

        Optional<SharedRoomEntity> sharedRoomsOptional = sharedRoomRepository.findById(0L);

//        if (!sharedRoomsOptional.isEmpty()) {
//            SharedRoomEntity sharedRoom = sharedRoomsOptional.get();
//            //get collaboratorDTO
////            List<String> collaboratorEmails = sharedRoom.getCollaboratorEmail();
////            for(String collaboratorEmail:collaboratorEmails) {
////                UserDTO collaboratorDTO = userService.getUserByEmail(collaboratorEmail);
////                collaborators.add(collaboratorDTO);
//            }
//        }
        return collaborators;
    }
}
