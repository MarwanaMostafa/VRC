package com.example.vrc.rooms.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Collaborator Deletion successfully"),
        @ApiResponse(responseCode = "400", description = "There is an issue with the request body"),
        @ApiResponse(responseCode = "404", description = "Resource Not found"),
        @ApiResponse(responseCode = "500", description = "There is a problem on the server")
})
public @interface ApiFullResponseDeleteCollaborator {
}
