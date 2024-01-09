package com.example.vrc.authentication.configs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;


@Configuration
@Profile("!Authentication")
public class AuthenticationSwaggerConfig {
    @Bean(name = "authenticationHttpApi")
    public GroupedOpenApi httpApi() {

        OpenAPI authOpenApi = apiInfoAuth();

        return GroupedOpenApi.builder()
                .group("AuthenticationVRC")
                .packagesToScan("com.example.vrc.authentication").
                addOpenApiCustomizer(openApi -> customizeOpenAPI(openApi, authOpenApi))
                .build();
    }

    private void customizeOpenAPI(OpenAPI openApi, OpenAPI apiInfo) {
        openApi.info(apiInfo.getInfo())
                .externalDocs(apiInfo.getExternalDocs()).servers(Collections.singletonList(
                        new io.swagger.v3.oas.models.servers.Server()
                                .url("https://vrc-production.up.railway.app")
                                .description("Production Server")));
    }

    private OpenAPI apiInfoAuth() {
        return new OpenAPI().info(new Info()
                        .title("VRC Authentication Service")
                        .description("REST APIs for authentication service.")
                        .version("0.0.0")
                        .contact(new Contact().name("Marwan Mostafa").email("MarwanMostafa2001@hotmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}