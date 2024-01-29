package com.example.vrc.authentication.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserWithoutPasswordDTO implements Serializable {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private String token;
}