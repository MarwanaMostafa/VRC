package com.example.vrc.authentication.DTOs;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordData {

    @Email(message = "Please provide a valid email address")
    private final String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "The field 'password' should have minimum 8 characters!", groups = Size.class)
    private final String password;

    @NotBlank(message = "Repeated Password cannot be blank")
    @Size(min = 8, message = "The field 'Repeated Password' should have minimum 8 characters!", groups = Size.class)
    private final  String repeatedPassword;

    // Constructors, getters, and setters


    public ResetPasswordData(String email, String password, String repeatedPassword) {
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }

    // Getters and setters for email, password, and repeatedPassword

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

}
