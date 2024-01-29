package com.example.vrc.rooms.models;

import com.example.vrc.authentication.models.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import java.util.UUID;

@Entity
@Table(name = "shared_rooms")
@Data
public class SharedRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private UserEntity collaborator;

    @Getter
    @ManyToOne(targetEntity = RoomEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

}
