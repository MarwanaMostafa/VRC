package com.example.vrc.rooms.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shared_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collaborator")
    private String collaborator;

    @ManyToOne
    @JoinColumn(name = "room_id")

    private RoomEntity room;

    public SharedRoomEntity(String collaboratorEmail, RoomEntity room) {
        this.collaborator=collaboratorEmail;
        this.room=room;
    }
}
