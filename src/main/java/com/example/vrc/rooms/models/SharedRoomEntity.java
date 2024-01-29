package com.example.vrc.rooms.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import lombok.Getter;
import java.util.UUID;

@Entity
@Data
public class SharedRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Getter
    @ElementCollection
    @CollectionTable(name = "shared_room_collaborators", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "collaborator_email", nullable = false)
    private List<String> collaboratorEmail;

    @Getter
    @ManyToOne(targetEntity = RoomEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

}
