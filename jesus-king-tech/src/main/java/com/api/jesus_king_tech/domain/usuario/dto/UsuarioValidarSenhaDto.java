package com.api.jesus_king_tech.domain.usuario.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioValidarSenhaDto {

    private String email;
    private String codigo_recuperar_senha;
}
