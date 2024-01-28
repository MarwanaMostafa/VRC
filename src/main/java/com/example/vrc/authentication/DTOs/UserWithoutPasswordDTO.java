package com.example.vrc.authentication.DTOs;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserWithoutPasswordDTO implements Serializable {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private String token;
}