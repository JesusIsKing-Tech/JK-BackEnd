package com.api.jesus_king_tech.api.observer;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AdminEmailObserver implements Observer {

    public static final List<String> ADMIN_EMAIL = Arrays.asList(
            "kauavidal2004@gmail.com"
    );

    @Override
    public void update(String eventType, Usuario usuario) {
        System.out.println("Entrei no update");
        if(eventType.equals("usuario_criado")) {
            System.out.println("Enviando email");
            for (String email : ADMIN_EMAIL){
                JavaMailObserver.sendEmail(email, usuario.getNome(), usuario.getEmail());
            }
            System.out.println("Enviando email para o admin: " + usuario.getEmail());
        }
    }
}
