package com.example.vrc.rooms.swagger;
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
        @ApiResponse(responseCode = "200", description = "Share Public room using room id  successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\" }"))),
        @ApiResponse(responseCode = "400", description = "There is wrong in request body (like ID not exist in request body)", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ }"))),
        @ApiResponse(responseCode = "404", description = "Resource Not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\" }"))),
        @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"id\": \"ID\" }")))
})
public @interface ApiFullResponseGetSharedRoom {
}
