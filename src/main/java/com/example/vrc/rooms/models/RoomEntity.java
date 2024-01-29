package com.example.vrc.rooms.models;

import jakarta.persistence.*;

import java.util.*;

import com.example.vrc.authentication.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "state", nullable = false, length = 50000)
    private String state;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<SharedRoomEntity> sharedRooms = new ArrayList<>();
}