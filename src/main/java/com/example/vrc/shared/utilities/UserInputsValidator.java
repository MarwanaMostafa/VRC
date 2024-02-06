package com.example.vrc.shared.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

public class UserInputsValidator {
    public static void validate(Errors errors) throws ResponseStatusException {
        if(!errors.hasErrors()) {
            return;
        }
        ObjectError firstError = errors.getAllErrors().get(0);

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, firstError.getDefaultMessage());
    }
}
