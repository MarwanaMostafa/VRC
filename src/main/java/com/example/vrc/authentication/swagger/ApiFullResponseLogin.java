package com.example.vrc.authentication.swagger;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"MarwanMostafa2001@hotmail.com\", \"password\": \"abc123\" }"))),
        @ApiResponse(responseCode = "400", description = "There is wrong in request body", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"MarwanMostafa2001@hotmail.com\" }"))),
        @ApiResponse(responseCode = "404", description = "Resource Not found ", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"String\", \"password\": \"String\" }"))),
        @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"email\": \"String\", \"password\": \"String\" }")))
})
public @interface ApiFullResponseLogin {
}