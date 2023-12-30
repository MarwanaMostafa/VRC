package com.example.vrc.authentication.services;

import com.example.vrc.authentication.DTOs.ResetPasswordData;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.models.RUserCredentials;

public interface AuthService {
    UserWithoutPasswordDTO signUp(UserDTO userDTO) throws ResponseStatusException;
    UserWithoutPasswordDTO login(RUserCredentials userCredentials) throws  ResponseStatusException;

    String forgotPassword(String email);
    String setPassword(ResetPasswordData resetPasswordData);
}
