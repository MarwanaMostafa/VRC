package com.example.vrc.rooms.DTOs;

import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.example.vrc.rooms.models.RoomEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO implements Serializable {
    private UUID id;
    private String title;
    private String description;
    private String state;
    private Boolean isPublic;
    private UserWithoutPasswordDTO user;
}