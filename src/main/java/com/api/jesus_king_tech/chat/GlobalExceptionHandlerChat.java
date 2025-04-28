package com.api.jesus_king_tech.chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerChat {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerChat.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpClientErrorException(HttpClientErrorException e) {
        logger.error("Erro na API externa: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Map.of("error", "Erro na API externa: " + e.getMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpServerErrorException(HttpServerErrorException e) {
        logger.error("Erro no servidor externo: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatusCode())
                .body(Map.of("error", "Erro no servidor externo: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        logger.error("Erro inesperado: {}", e.getMessage(), e);
        return ResponseEntity
                .status(500)
                .body(Map.of("error", "Erro inesperado: " + e.getMessage()));
    }
}