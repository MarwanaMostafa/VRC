package com.example.vrc.services;

import com.example.vrc.DTOs.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    UserDTO getUserByEmail(String userEmail);

     UserDetails loadUserByUsername(String Email);
}
