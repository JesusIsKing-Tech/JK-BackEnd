package com.api.jesus_king_tech.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite todas as rotas
                        .allowedOrigins("*") // Permite todas as origens
                        .allowedMethods("*") // Permite todos os métodos HTTP
                        .allowedHeaders("*") // Permite todos os cabeçalhos
                        .allowCredentials(false); // Desabilita envio de cookies
            }
        };
    }
}