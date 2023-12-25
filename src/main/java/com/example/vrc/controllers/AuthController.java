package com.example.vrc.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.DTOs.UserDTO;
import com.example.vrc.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.models.RUserCredentials;
import com.example.vrc.services.AuthService;
import com.example.vrc.utilities.UserInputsValidator;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserWithoutPasswordDTO> signUp(@Valid @RequestBody UserDTO userDTO, Errors errors) throws RuntimeException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.authService.signUp(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserWithoutPasswordDTO> login(@Valid @RequestBody RUserCredentials userCredentials, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.authService.login(userCredentials), HttpStatus.OK);
    }
}
