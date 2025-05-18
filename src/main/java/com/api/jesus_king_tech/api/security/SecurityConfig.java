package com.api.jesus_king_tech.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    Configurações de springSecurity

//    @Bean
//    public SecurityFilterChain web(HttpSecurity http) throws Exception {
//        System.out.println("Permissão geral adicionada");
//        http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
//
//        return http.build();
//    }

    //    Configuração de hash de senhas
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    Configuração de CORS para o VITE (front)

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
