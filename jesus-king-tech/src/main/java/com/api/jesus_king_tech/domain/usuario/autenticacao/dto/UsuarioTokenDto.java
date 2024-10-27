package com.api.jesus_king_tech.domain.usuario.autenticacao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class UsuarioTokenDto {
    private int userId;
    private String nome;
    private String email;
    private String token;
}
