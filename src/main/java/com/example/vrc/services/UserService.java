package com.example.vrc.services;

import com.example.vrc.DTOs.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    UserDTO getUserByEmail(String userEmail);
}
