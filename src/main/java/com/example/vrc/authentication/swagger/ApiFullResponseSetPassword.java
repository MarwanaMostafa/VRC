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
        @ApiResponse(responseCode = "200", description = "Set Password successful use new password when login again", content = @Content(examples = @ExampleObject("{\"password\": \"12341234\",\n \"repeatedPassword\": \"12341234\"\n}"))),
        @ApiResponse(responseCode = "400", description = "There is wrong in request body (don't put repeated password)", content = @Content(examples = @ExampleObject("{\"password\": \"12341234\"\n}"))),
        @ApiResponse(responseCode = "404", description = "Resource Not found ", content = @Content(examples = @ExampleObject("{\"password\": \"String\",\n \"repeatedPassword\": \"String\"\n}"))),
        @ApiResponse(responseCode = "500", description = "There is problem in server", content = @Content(examples = @ExampleObject("{\"password\": \"String\",\n \"repeatedPassword\": \"String\"\n}")))
})
public @interface ApiFullResponseSetPassword {
}