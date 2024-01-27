package com.example.vrc.rooms.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class RoomIDDTO {
    private UUID roomID;

    public UUID getRoomID() {
        return roomID;
    }
}
