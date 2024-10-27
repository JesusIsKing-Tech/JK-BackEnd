package com.api.jesus_king_tech.util;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component //esse @component é para mostrar que ele é um componente da validação, sem ele não funciona essa validação
public class EmailUtil implements ValidacaoUsuarioStrategy {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean validar(Usuario usuario) {

        if (usuario.getEmail() == null) {
            return false;
        }
        return pattern.matcher(usuario.getEmail()).matches();
    }

    @Override
    public String respostaErro() {
        return "O email fornecido é inválido. Por favor, insira um email no formato correto, como exemplo@dominio.com.";
    }
}

