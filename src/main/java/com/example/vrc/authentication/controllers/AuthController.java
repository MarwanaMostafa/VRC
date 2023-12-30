package com.example.vrc.authentication.controllers;

import com.example.vrc.authentication.DTOs.ResetPasswordData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.services.AuthService;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.authentication.models.RUserCredentials;
import com.example.vrc.authentication.utilities.UserInputsValidator;

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

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email ) {
        return new ResponseEntity<>(this.authService.forgotPassword(email), HttpStatus.OK);
    }
    @PutMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody ResetPasswordData resetPasswordData ) {
        return new ResponseEntity<>(this.authService.setPassword(resetPasswordData), HttpStatus.OK);
    }

}
