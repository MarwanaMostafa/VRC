package com.example.vrc.authentication.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")

                .allowedOrigins("https://vrc-production.up.railway.app/swagger-ui/**") // Replace with your Swagger UI URL
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
