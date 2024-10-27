package com.api.jesus_king_tech.domain.usuario.autenticacao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioLoginDto {
    private String email;
    private String senha;
}
