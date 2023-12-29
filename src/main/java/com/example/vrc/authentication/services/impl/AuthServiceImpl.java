package com.example.vrc.authentication.services.impl;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.utilities.JwtUtil;
import com.example.vrc.authentication.utilities.UserPasswordEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.authentication.services.AuthService;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;
import com.example.vrc.authentication.models.RUserCredentials;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserWithoutPasswordMapper userWithoutPasswordMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserWithoutPasswordDTO signUp(UserDTO userDTO) throws ResponseStatusException {
        UserDTO user = this.userService.getUserByEmail(userDTO.getEmail());

        if(user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There's already a user with the entered email!");
        }

        user = this.userService.createUser(new UserDTO(
                userDTO.getId(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                UserPasswordEncryption.encodePassword(userDTO.getPassword())
        ));

        return this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(user));
    }

    @Override
    public UserWithoutPasswordDTO login(RUserCredentials userCredentials) throws ResponseStatusException {
        UserDTO user = this.userService.getUserByEmail(userCredentials.email());

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }

        if(!UserPasswordEncryption.matchPasswords(userCredentials.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect Password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        UserWithoutPasswordDTO userWithoutPasswordDTO = this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(user));
        userWithoutPasswordDTO.setToken(token);
        return userWithoutPasswordDTO;    }
}
