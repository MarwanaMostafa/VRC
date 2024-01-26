package com.example.vrc.rooms.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.apache.catalina.User;

@Data
public class SharedRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false)
    private User collaboratorEmail;

    @Column(nullable = false)
    private RoomEntity room;

}
