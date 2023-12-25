package com.example.vrc.services;

import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.DTOs.UserDTO;
import com.example.vrc.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.models.RUserCredentials;

public interface AuthService {
    UserWithoutPasswordDTO signUp(UserDTO userDTO) throws ResponseStatusException;
    UserWithoutPasswordDTO login(RUserCredentials userCredentials) throws  ResponseStatusException;
}
