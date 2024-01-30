package com.example.vrc.authentication.services;

import com.example.vrc.authentication.DTOs.RUserCredentials;
import com.example.vrc.authentication.DTOs.ResetPasswordData;
import org.springframework.web.server.ResponseStatusException;
import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;

public interface AuthService {
    UserWithoutPasswordDTO signUp(UserDTO userDTO) throws ResponseStatusException;
    UserWithoutPasswordDTO login(RUserCredentials userCredentials) throws  ResponseStatusException;
    UserWithoutPasswordDTO autoLogin(String email) throws  ResponseStatusException;
    String forgotPassword(String email);
    String setPassword(ResetPasswordData resetPasswordData, String token);
}
