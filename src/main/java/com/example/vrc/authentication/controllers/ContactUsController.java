package com.example.vrc.authentication.controllers;

import com.example.vrc.authentication.DTOs.ContactUsDTO;
import com.example.vrc.authentication.services.ContactUsService;
import com.example.vrc.authentication.swagger.ApiFullResponseCreateComplaint;
import com.example.vrc.shared.utilities.UserInputsValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.vrc.authentication.common.documentation.DocConstant.AuthenticationConstants.*;

@Tag(name = API_NAME, description = API_DESCRIPTION)
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "AddToken")
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @Operation(summary = API_CONTACTUS_NAME, description =API_CONTACTUS_DESCRIPTION)
    @PostMapping("/create-complaint")
    @ApiFullResponseCreateComplaint
    ResponseEntity<String> createComplaint(@Valid @RequestBody ContactUsDTO contactUsDTO,Errors errors) throws ResponseStatusException {
        UserInputsValidator.validate(errors);
        return new ResponseEntity<>(this.contactUsService.createComplain(contactUsDTO), HttpStatus.CREATED);
    }
}

