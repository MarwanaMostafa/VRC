//package com.example.vrc.authentication.configs;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Specify the URL pattern for which CORS should be configured
//                .allowedOrigins("https://vrc-production.up.railway.app", "https://vrc-production.up.railway.app")
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed HTTP methods
//                .allowedHeaders("Origin", "Content-Type", "Accept") // Specify the allowed headers
//                .allowCredentials(true); // Allow credentials such as cookies
//    }
//}