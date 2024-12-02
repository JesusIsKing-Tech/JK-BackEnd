package com.api.jesus_king_tech.exception;

import jakarta.persistence.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionHttp.class)
    public ResponseEntity<Map<String, Object>> handleExceprionHttp(ExceptionHttp exception, WebRequest request){


        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", exception.getStatus().value());
        response.put("error", exception.getStatus().getReasonPhrase());
        response.put("message", exception.getMessage());
        response.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(exception.getStatus()).body(response);

    }
}
