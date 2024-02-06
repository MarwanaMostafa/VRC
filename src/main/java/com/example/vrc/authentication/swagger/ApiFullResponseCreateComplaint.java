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
        @ApiResponse(responseCode = "200", description = "Complaint created successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"firstName\", \"secondName\": \"secondName\", \"email\": \"email\", \"complaint\": \"complaint\" }"))),
        @ApiResponse(responseCode = "400", description = "There is wrong in request body (like email not exist in request body)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"firstName\", \"secondName\": \"secondName\", \"complaint\": \"complaint\" }"))),
        @ApiResponse(responseCode = "404", description = "Resource Not found",content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"firstName\", \"secondName\": \"secondName\", \"email\": \"email\", \"complaint\": \"complaint\" }"))),
        @ApiResponse(responseCode = "500", description = "There is problem in server",content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"firstName\": \"firstName\", \"secondName\": \"secondName\", \"email\": \"email\", \"complaint\": \"complaint\" }")))
})
public @interface ApiFullResponseCreateComplaint {
}

