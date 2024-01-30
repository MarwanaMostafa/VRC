package com.example.vrc.rooms.models;

import com.example.vrc.authentication.models.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "shared_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedRoomEntity {


    @Column(name = "collaborator")
    @Id
    private String collaborator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")

    private RoomEntity room;

}
