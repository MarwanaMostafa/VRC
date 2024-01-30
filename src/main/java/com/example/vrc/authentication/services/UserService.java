package com.example.vrc.authentication.services;

import com.example.vrc.authentication.DTOs.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    public void changePassword(String userEmail , String newPassword) ;
    UserDTO getUserByEmail(String userEmail);

    UserDetails loadUserByUsername(String Email);
}
