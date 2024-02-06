package com.example.vrc.rooms.repositories;

import com.example.vrc.rooms.models.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
    List<RoomEntity> findAllByUserEmailIgnoreCase(String userEmail);
    RoomEntity findByUserEmailIgnoreCaseAndId(String userEmail,UUID uuid);
}