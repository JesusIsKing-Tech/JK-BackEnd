package com.api.jesus_king_tech.util;

import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

public class EmailUtil {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean emailValido(String email){
        if (email == null){
            return false;
        }
        return pattern.matcher(email).matches();
    }
}

