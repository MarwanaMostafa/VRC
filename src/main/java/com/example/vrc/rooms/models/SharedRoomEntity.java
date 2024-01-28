package com.example.vrc.rooms.models;

import com.example.vrc.authentication.models.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import org.apache.catalina.User;

import java.util.UUID;

@Entity
@Data
public class SharedRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "shared_room_collaborators", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "collaborator_email", nullable = false)
    private List<String> collaboratorEmail;

    @ManyToOne(targetEntity = RoomEntity.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    public List<String> getCollaboratorEmail() {
        return collaboratorEmail;
    }

    public RoomEntity getRoom(){
        return this.room;
    }


}
