package com.example.vrc;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@SecurityScheme(name = "AddToken", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER,bearerFormat = "JWT")

public class VrcApplication {

	public static void main(String[] args) {
		SpringApplication.run(VrcApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://your-frontend-domain.com")
						.allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
						.allowedHeaders("Origin", "Content-Type", "Accept")
						.allowCredentials(true);			}
		};
	}
}
