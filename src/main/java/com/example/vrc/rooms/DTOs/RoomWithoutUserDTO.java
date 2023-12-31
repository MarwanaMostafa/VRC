package com.example.vrc.rooms.DTOs;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link com.example.vrc.rooms.models.RoomEntity}
 */

@GroupSequence({ NotEmpty.class, NotNull.class, Size.class, RoomWithoutUserDTO.class })
public class RoomWithoutUserDTO implements Serializable {
    private final UUID id;

    @NotEmpty(message = "The field 'title' is required!", groups = NotEmpty.class)
    @Size(min = 2, message = "The field 'title' should have minimum 2 characters!", groups = Size.class)
    @Size(max = 50, message = "The field 'title' should have maximum 50 characters!", groups = Size.class)
    private final String title;

    @NotEmpty(message = "The field 'description' is required!", groups = NotEmpty.class)
    @Size(min = 10, message = "The field 'description' should have minimum 10 characters!", groups = Size.class)
    @Size(max = 250, message = "The field 'description' should have maximum 250 characters!", groups = Size.class)
    private final String description;

    @NotNull(message = "The field 'state' is required!", groups = NotNull.class)
    private final String state;

    @NotNull(message = "The field 'isPublic' is required!", groups = NotNull.class)
    private final Boolean isPublic;

    public RoomWithoutUserDTO(UUID id, String title, String description, String state, Boolean isPublic) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.isPublic = isPublic;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomWithoutUserDTO entity = (RoomWithoutUserDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.state, entity.state) &&
                Objects.equals(this.isPublic, entity.isPublic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, state, isPublic);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "description = " + description + ", " +
                "state = " + state + ", " +
                "isPublic = " + isPublic + ")";
    }
}