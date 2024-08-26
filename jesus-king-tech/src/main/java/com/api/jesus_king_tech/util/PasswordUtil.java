package com.api.jesus_king_tech.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

public class PasswordUtil {

    public static String encoder(String senha){
        BCryptPasswordEncoder encoderSenha = new BCryptPasswordEncoder();
        return encoderSenha.encode(senha);
    }

    public static boolean senhaCorreta(String senha, String senhaEncoded){
        BCryptPasswordEncoder encoderSenha = new BCryptPasswordEncoder();
        return encoderSenha.matches(senha, senhaEncoded);
    }

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean senhaValida(String senha){
        if (senha == null){
            return false;
        }
        return pattern.matcher(senha).matches();
    }
}
