package com.api.jesus_king_tech.api.observer;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;

public interface Observer {
    void update(String eventType, Usuario usuario);
}
