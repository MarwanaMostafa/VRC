package com.example.vrc.authentication.DTOs;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({ NotEmpty.class, Size.class, Email.class, ContactUsDTO.class })
public class ContactUsDTO {
    @NotEmpty(message = "The field 'firstName' is required!")
    @Size(min = 3, max = 20, message = "The field 'firstName' should have 3 to 20 letters!")
    private String firstName;

    @NotEmpty(message = "The field 'secondName' is required!")
    @Size(min = 3, max = 20, message = "The field 'secondName' should have 3 to 20 letters!")
    private String secondName;

    @NotEmpty(message = "The field 'email' is required!")
    @Email(message = "The field 'email' should be a valid email!")
    private String email;

    @NotEmpty(message = "The field 'complaint' is required!")
    @Size(min = 5, message = "The field 'complaint' should have minimum 10 letters!")
    private String complaint;
}
