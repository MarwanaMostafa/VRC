package com.example.vrc.authentication.controllers;

import com.example.vrc.authentication.DTOs.ResetPasswordData;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
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
import com.example.vrc.authentication.models.RUserCredentials;
import com.example.vrc.shared.utilities.UserInputsValidator;

import static com.example.vrc.authentication.common.documentation.DocConstant.AuthenticationConstants.*;
@Tag(name = API_NAME,description = API_DESCRIPTION)
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;


    @Operation(summary = API_POST_LOG_IN_VALUES,description = API_POST_LOG_IN_DESCRIPTION)
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode  = "200", description = "Login successful", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"MarwanMostafa2001@hotmail.com\", \"password\": \"abc123\" }"))),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"MarwanMostafa2001@hotmail.com\" }"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found ", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"String\", \"password\": \"String\" }"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"String\", \"password\": \"String\" }")))
    })
    public ResponseEntity<UserWithoutPasswordDTO> login(@Valid @RequestBody RUserCredentials userCredentials, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.authService.login(userCredentials), HttpStatus.OK);
    }

    @Operation(summary = API_POST_SIGN_UP_VALUES,description = API_POST_SIGN_UP_DESCRIPTION)
    @PostMapping("/sign-up")
    @CrossOrigin(origins = "http://allowed-origin.com")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"Marwan\", \"lastName\": \"Mostafa\", \"email\": \"MarwanMostafa2001@hotmail.com\", \"password\": \"abc123\" }"))),
            @ApiResponse(responseCode= "400", description = "There is wrong in request body (like email not exist in request body)",content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"Marwan\", \"lastName\": \"Mostafa\", \"password\": \"abc123\" }"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"String\", \"lastName\": \"String\", \"email\": \"String\", \"password\": \"String\" }"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"String\", \"lastName\": \"String\", \"email\": \"String\", \"password\": \"String\" }")))
    })
    public ResponseEntity<UserWithoutPasswordDTO> signUp(@Valid @RequestBody UserDTO userDTO, Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.authService.signUp(userDTO), HttpStatus.CREATED);
    }

    @Operation(summary = API_GET_AUTO_LOG_IN_VALUES, description = API_GET_AUTO_LOG_IN_DESCRIPTION)
    @GetMapping("/auto-login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto Login successful"),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body"),
            @ApiResponse(responseCode = "404", description = "Resource Not found "),
            @ApiResponse(responseCode = "500", description = "There is problem in server")
    })
    public ResponseEntity<UserWithoutPasswordDTO> autoLogin(Authentication auth) {
        String userEmail = auth.getName();
        return new ResponseEntity<>(this.authService.autoLogin(userEmail), HttpStatus.OK);
    }

    @Operation(summary = API_PUT_FORGET_PASSWORD_VALUES, description = API_PUT_FORGET_PASSWORD_DESCRIPTION)
    @PutMapping("/forgot-password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forgot Password  successful Please check your email",content = @Content (examples = @ExampleObject(value="{\"email\": \"marwanmostafa2001@hotmail.com\"}"))),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body (don't put email)",content = @Content(examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found ",content = @Content (examples = @ExampleObject(value="{\"email\": \"String\"}"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server",content = @Content (examples = @ExampleObject(value="{\"email\": \"String\"}")))
    })
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return new ResponseEntity<>(this.authService.forgotPassword(email), HttpStatus.OK);
    }

    @Operation(summary = API_PUT_SET_PASSWORD_VALUES, description = API_PUT_SET_PASSWORD_DESCRIPTION)
    @PutMapping("/set-password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Set Password successful use new password when login again",content = @Content(examples =@ExampleObject ("{\"password\": \"12341234\",\n \"repeatedPassword\": \"12341234\"\n}"))),
            @ApiResponse(responseCode = "400", description = "There is wrong in request body (don't put repeated password)",content = @Content(examples =@ExampleObject ("{\"password\": \"12341234\"\n}"))),
            @ApiResponse(responseCode = "404", description = "Resource Not found ",content = @Content(examples =@ExampleObject ("{\"password\": \"String\",\n \"repeatedPassword\": \"String\"\n}"))),
            @ApiResponse(responseCode = "500", description = "There is problem in server",content = @Content(examples =@ExampleObject ("{\"password\": \"String\",\n \"repeatedPassword\": \"String\"\n}")))
    })
    public ResponseEntity<String> setPassword(@RequestBody ResetPasswordData resetPasswordData) {
        return new ResponseEntity<>(this.authService.setPassword(resetPasswordData), HttpStatus.OK);
    }

}
