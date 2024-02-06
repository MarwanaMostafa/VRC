package com.example.vrc.rooms.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedRoomDTO {
    @NotEmpty
    @Email
    private String collaboratorEmail;
    @NotEmpty
    private String id;
}
