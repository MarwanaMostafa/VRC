//package com.example.vrc.assets.configs;
//
//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//
//@Configuration
//@Profile("!Assets")
//
//public class AssetsSwaggerConfig {
//
//
//    @Bean(name = "AssetsHttpApi")
//    public GroupedOpenApi httpApi() {
//        OpenAPI assetsOpenApi = apiInfoAssets();
//
//        return GroupedOpenApi.builder()
//                .group("AssetsVRC")
//                .packagesToScan("com.example.vrc.assets")
//                .addOpenApiCustomizer(openApi -> customizeOpenAPI(openApi,assetsOpenApi))
//                .build();
//    }
//    private void customizeOpenAPI(OpenAPI openApi, OpenAPI apiInfoAssets) {
//        openApi.info(apiInfoAssets.getInfo())
//                .externalDocs(apiInfoAssets.getExternalDocs());
//    }
//    private  OpenAPI apiInfoAssets(){
//        return new OpenAPI().info(new Info()
//                        .title("VRC Assets Service")
//                        .description("REST APIs for Assets service.")
//                        .version("0.0.0")
//                        .contact(new Contact().name("Marwan Mostafa").email("MarwanMostafa2001@hotmail.com")))
//                .externalDocs(new ExternalDocumentation()
//                        .description("SpringShop Wiki Documentation")
//                        .url("https://springshop.wiki.github.org/docs"));
//    }
//
//}
