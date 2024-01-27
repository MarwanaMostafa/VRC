package com.example.vrc.rooms.repositories;

import com.example.vrc.rooms.models.RoomEntity;
import com.example.vrc.rooms.models.SharedRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedRoomRepository extends JpaRepository<SharedRoomEntity, UUID> {
    List<SharedRoomEntity> findAllByCollaboratorEmailIgnoreCase(String userEmail);
}