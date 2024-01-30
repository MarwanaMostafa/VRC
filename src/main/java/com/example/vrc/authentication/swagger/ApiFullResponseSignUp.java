package com.example.vrc.authentication.swagger;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Please validate your request body"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
        @ApiResponse(responseCode = "404", description = "The resource not found"),
        @ApiResponse(responseCode = "500", description = "Something went wrong")
})
public @interface ApiFullResponseSignUp {
}