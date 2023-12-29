package com.example.vrc.authentication.services;

import com.example.vrc.authentication.DTOs.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    UserDTO getUserByEmail(String userEmail);

     UserDetails loadUserByUsername(String Email);
}
