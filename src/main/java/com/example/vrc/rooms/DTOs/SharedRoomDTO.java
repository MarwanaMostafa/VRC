package com.example.vrc.rooms.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedRoomDTO {

    @NotEmpty
    @Email
    private String collaboratorEmail;

    @NotEmpty
    private UUID id;


    public SharedRoomDTO(UUID id,String collaboratorEmail){
        this.id=id;
        this.collaboratorEmail=collaboratorEmail;
    }
    public UUID getId() {
        return id;
    }

}
