package com.example.vrc.rooms.DTOs;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
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
@GroupSequence({ NotEmpty.class, NotNull.class, Size.class, RoomWithoutUserDTO.class })
public class RoomWithoutUserDTO implements Serializable {
    private  UUID id;
    private Long ownerId;
    @NotEmpty(message = "The field 'title' is required!", groups = NotEmpty.class)
    @Size(min = 2, message = "The field 'title' should have minimum 2 characters!", groups = Size.class)
    @Size(max = 50, message = "The field 'title' should have maximum 50 characters!", groups = Size.class)
    private  String title;

    @NotEmpty(message = "The field 'description' is required!", groups = NotEmpty.class)
    @Size(min = 10, message = "The field 'description' should have minimum 10 characters!", groups = Size.class)
    @Size(max = 250, message = "The field 'description' should have maximum 250 characters!", groups = Size.class)
    private  String description;

    @NotNull(message = "The field 'state' is required!", groups = NotNull.class)
    private  String state;

    @NotNull(message = "The field 'isPublic' is required!", groups = NotNull.class)
    private  Boolean isPublic;
}