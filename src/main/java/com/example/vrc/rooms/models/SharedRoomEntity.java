package com.example.vrc.rooms.models;

import com.example.vrc.authentication.models.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.User;

@Entity
@Data
public class SharedRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String collaboratorEmail;

    @ManyToOne(targetEntity = RoomEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    public String getCollaboratorEmail() {
        return collaboratorEmail;
    }

    public RoomEntity getRoom(){
        return this.room;
    }


}
