package com.example.vrc.rooms.DTOs;

import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link com.example.vrc.rooms.models.RoomEntity}
 */
public class RoomDTO implements Serializable {
    private final UUID id;
    private final String title;
    private final String description;
    private final String state;
    private final Boolean isPublic;
    private final UserWithoutPasswordDTO user;

    public RoomDTO(UUID id, String title, String description, String state, Boolean isPublic, UserWithoutPasswordDTO user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.isPublic = isPublic;
        this.user = user;
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

    public UserWithoutPasswordDTO getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDTO entity = (RoomDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.state, entity.state) &&
                Objects.equals(this.isPublic, entity.isPublic) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, state, isPublic, user);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "description = " + description + ", " +
                "state = " + state + ", " +
                "isPublic = " + isPublic + ", " +
                "user = " + user + ")";
    }
}