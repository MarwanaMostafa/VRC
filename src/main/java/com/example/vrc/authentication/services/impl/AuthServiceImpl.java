package com.example.vrc.authentication.services.impl;

import com.example.vrc.authentication.DTOs.RUserCredentials;
import com.example.vrc.authentication.DTOs.ResetPasswordData;
import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.Exception.PasswordMismatchException;
import com.example.vrc.authentication.utilities.EmailUtil;
import com.example.vrc.authentication.utilities.JwtUtil;
import com.example.vrc.authentication.utilities.UserPasswordEncryption;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.authentication.services.AuthService;
import com.example.vrc.authentication.services.UserService;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.mappers.UserWithoutPasswordMapper;

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
    @Autowired
    EmailUtil emailUtil;

    @Override
    public UserWithoutPasswordDTO signUp(UserDTO userDTO) throws ResponseStatusException {
        UserDTO user = this.userService.getUserByEmail(userDTO.getEmail());

        if (user != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There's already a user with the entered email!");
        }

        user = this.userService.createUser(new UserDTO(
                userDTO.getId(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                UserPasswordEncryption.encodePassword(userDTO.getPassword())
        ));

        String token = jwtUtil.generateToken(user.getEmail());

        UserWithoutPasswordDTO userWithoutPasswordDTO = this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(user));
        userWithoutPasswordDTO.setToken(token);

        return userWithoutPasswordDTO;
    }

    @Override
    @Cacheable(value = "userCache", key = "#userCredentials.email + '-' + #userCredentials.password")
    public UserWithoutPasswordDTO login(RUserCredentials userCredentials) throws ResponseStatusException {
        UserDTO user = this.userService.getUserByEmail(userCredentials.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }
        if (!UserPasswordEncryption.matchPasswords(userCredentials.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect Password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        UserWithoutPasswordDTO userWithoutPasswordDTO = this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(user));
        userWithoutPasswordDTO.setToken(token);
        return userWithoutPasswordDTO;
    }

    @Override
    @Cacheable(value = "userCache", key = "#email")
    public UserWithoutPasswordDTO autoLogin(String email) throws ResponseStatusException {
        UserDTO user = this.userService.getUserByEmail(email);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no user with the entered email!");
        }

        return this.userWithoutPasswordMapper.toDto(this.userMapper.toEntity(user));
    }

    @Override
    public String forgotPassword(String email) {
        UserDTO user = this.userService.getUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email :" + email);
        }
        try {
            emailUtil.sendSetEmailPassword(user.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send set password email");
        }
        return "Please check your email to set password";
    }

    @Override
    public String setPassword(ResetPasswordData resetPasswordData, String token) {
        String email="";
        try {
            email=jwtUtil.extractUsername(token);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        UserDTO user = this.userService.getUserByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email :" + email);
        }
        if (!resetPasswordData.getRepeatedPassword().equals(resetPasswordData.getPassword())) {
            throw new PasswordMismatchException();
        }
        this.userService.changePassword(email, UserPasswordEncryption.encodePassword(resetPasswordData.getPassword()));

        return "New password set successfully login with new password";
    }

}
