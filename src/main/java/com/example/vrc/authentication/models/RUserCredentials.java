package com.example.vrc.authentication.models;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

@GroupSequence({ NotEmpty.class, NotNull.class, RUserCredentials.class })
public record RUserCredentials(
    @NotEmpty(message = "The field 'email' is required!", groups = NotEmpty.class)
    String email,

    @NotEmpty(message = "The field 'password' is required!", groups = NotNull.class)
    String password
) {}
