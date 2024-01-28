package com.example.vrc.authentication.DTOs;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordData {

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "The field 'password' should have minimum 8 characters!", groups = Size.class)
    private String password;

    @NotBlank(message = "Repeated Password cannot be blank")
    @Size(min = 8, message = "The field 'Repeated Password' should have minimum 8 characters!", groups = Size.class)
    private String repeatedPassword;

}
