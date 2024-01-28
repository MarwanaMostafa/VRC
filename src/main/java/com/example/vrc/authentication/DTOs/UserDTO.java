package com.example.vrc.authentication.DTOs;

import com.example.vrc.authentication.models.UserEntity;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link UserEntity}
 */
@Data
@AllArgsConstructor
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
    private String password;

}