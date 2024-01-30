package com.example.vrc.authentication.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RUserCredentials {
    @NotEmpty(message = "The field 'email' is required!", groups = NotEmpty.class)
    private String email;

    @NotEmpty(message = "The field 'password' is required!", groups = NotNull.class)
    private String password;

}
