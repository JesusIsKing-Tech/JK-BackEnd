package com.api.jesus_king_tech.util;

import com.api.jesus_king_tech.cadastro_login.Usuario;
import org.springframework.stereotype.Component;

@Component
public interface ValidacaoUsuarioStrategy{
        boolean validar(Usuario usuario);
        String respostaErro();
}

