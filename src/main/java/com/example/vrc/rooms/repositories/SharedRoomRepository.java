package com.example.vrc.rooms.repositories;

import com.example.vrc.rooms.models.SharedRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedRoomRepository extends JpaRepository<SharedRoomEntity, Long> {
    List<SharedRoomEntity> findAllByCollaboratorIgnoreCase(String userEmail);
    SharedRoomEntity findByRoom_IdAndAndCollaboratorIgnoreCase(UUID room, String collaborator);
    List<SharedRoomEntity> findByRoom_Id(UUID roomID);
}