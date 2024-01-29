package com.example.vrc.rooms.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get all shared rooms successfully"),
        @ApiResponse(responseCode = "400", description = "There is wrong in request body"),
        @ApiResponse(responseCode = "404", description = "Resource Not found"),
        @ApiResponse(responseCode = "500", description = "There is problem in server")
})
public @interface ApiFullResponseGetSharedRooms {
}
