package com.example.vrc.authentication.controllers;

import com.example.vrc.authentication.DTOs.RUserCredentials;
import com.example.vrc.authentication.DTOs.ResetPasswordData;
import com.example.vrc.authentication.swagger.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.services.AuthService;
import com.example.vrc.authentication.DTOs.UserWithoutPasswordDTO;
import com.example.vrc.shared.utilities.UserInputsValidator;

import static com.example.vrc.authentication.common.documentation.DocConstant.AuthenticationConstants.*;

@Tag(name = API_NAME, description = API_DESCRIPTION)
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "AddToken")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Operation(summary = API_POST_LOG_IN_VALUES, description = API_POST_LOG_IN_DESCRIPTION)
    @PostMapping("/login")
    @ApiFullResponseLogin
    public ResponseEntity<UserWithoutPasswordDTO> login(@Valid @RequestBody RUserCredentials userCredentials, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.authService.login(userCredentials), HttpStatus.OK);
    }

    @Operation(summary = API_POST_SIGN_UP_VALUES, description = API_POST_SIGN_UP_DESCRIPTION)
    @PostMapping("/sign-up")
    @ApiFullResponseSignUp
    public ResponseEntity<UserWithoutPasswordDTO> signUp(@Valid @RequestBody UserDTO userDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.authService.signUp(userDTO), HttpStatus.CREATED);
    }

    @Operation(summary = API_GET_AUTO_LOG_IN_VALUES, description = API_GET_AUTO_LOG_IN_DESCRIPTION)
    @GetMapping("/auto-login")
    @ApiFullResponseAutoLogin
    public ResponseEntity<UserWithoutPasswordDTO> autoLogin(Authentication auth) {
        String userEmail = auth.getName();
        return new ResponseEntity<>(this.authService.autoLogin(userEmail), HttpStatus.OK);
    }

    @Operation(summary = API_PUT_FORGET_PASSWORD_VALUES, description = API_PUT_FORGET_PASSWORD_DESCRIPTION)
    @GetMapping("/forgot-password")
    @ApiFullResponseForgetPassword
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(this.authService.forgotPassword(email), HttpStatus.OK);
    }

    @Operation(summary = API_PUT_SET_PASSWORD_VALUES, description = API_PUT_SET_PASSWORD_DESCRIPTION)
    @PostMapping("/set-password/{token}")
    @ApiFullResponseSetPassword
    public ResponseEntity<String> setPassword(@PathVariable String token, @RequestBody ResetPasswordData resetPasswordData) {

         return new ResponseEntity<>(this.authService.setPassword(resetPasswordData, token), HttpStatus.OK);
    }


}
