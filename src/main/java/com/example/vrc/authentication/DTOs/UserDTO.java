package com.example.vrc.authentication.DTOs;

import com.example.vrc.authentication.models.UserEntity;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link UserEntity}
 */

@GroupSequence({ NotEmpty.class, Size.class, Pattern.class, Email.class, UserDTO.class })
public class UserDTO implements Serializable {
    private final Long id;

    @NotEmpty(message = "The field 'firstName' is required!", groups = NotEmpty.class)
    @Size(min = 3, message = "The field 'firstName' should have minimum 3 letters!", groups = Size.class)
    @Size(max = 20, message = "The field 'firstName' should have maximum 20 letters!", groups = Size.class)
    @Pattern(regexp = "^[A-Za-z]+$", message = "The field 'firstName' should contain only letters!", groups = Pattern.class)
    private final String firstName;


    @NotEmpty(message = "The field 'lastName' is required!", groups = NotEmpty.class)
    @Size(min = 3, message = "The field 'lastName' should have minimum 3 letters!", groups = Size.class)
    @Size(max = 20, message = "The field 'lastName' should have maximum 20 letters!", groups = Size.class)
    @Pattern(regexp = "^[A-Za-z]+$", message = "The field 'lastName' should contain only letters!", groups = Pattern.class)
    private final String lastName;

    @NotEmpty(message = "The field 'email' is required!", groups = NotEmpty.class)
    @Email(message = "The field 'email' should be a valid email!", groups = Email.class)
    private final String email;

    @NotEmpty(message = "The field 'password' is required!", groups = NotEmpty.class)
    @Size(min = 8, message = "The field 'password' should have minimum 8 characters!", groups = Size.class)
    private final String password;

    public UserDTO(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO entity = (UserDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.firstName, entity.firstName) &&
                Objects.equals(this.lastName, entity.lastName) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.password, entity.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "email = " + email + ", " +
                "password = " + password + ")";
    }
}