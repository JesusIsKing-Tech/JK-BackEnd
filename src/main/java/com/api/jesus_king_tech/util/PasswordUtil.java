package com.api.jesus_king_tech.util;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordUtil extends BCryptPasswordEncoder implements ValidacaoUsuarioStrategy {

    public static String encoder(String senha) {
        BCryptPasswordEncoder encoderSenha = new BCryptPasswordEncoder();
        return encoderSenha.encode(senha);
    }

    public static boolean matches(String senha, String senhaEncoded) {
        BCryptPasswordEncoder encoderSenha = new BCryptPasswordEncoder();
        return encoderSenha.matches(senha, senhaEncoded);
    }

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean validarSenha(String senha) {
        if (senha == null) {
            return false;
        }
        return pattern.matcher(senha).matches();
    }
    public static String respostaErroSenha() {
        return """
                Senha invalida, a senha deve conter:
                1 letra maiúscula,
                1 letra minúscula,
                1 número,
                1 caractere especial,
                no minimo 6 caracteres.
                """;
    }

    @Override
    public boolean validar(Usuario usuario) {
        if (usuario.getSenha() == null) {
            return false;
        }
        return pattern.matcher(usuario.getSenha()).matches();
    }

    @Override
    public String respostaErro() {
        return """
                Senha invalida, a senha deve conter:
                1 letra maiúscula,
                1 letra minúscula,
                1 número,
                1 caractere especial,
                no minimo 6 caracteres.
                """;
    }

}
