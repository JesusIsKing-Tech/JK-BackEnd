package com.api.jesus_king_tech.domain.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UsuarioMudarSenhaDto {

    private String email;
    private String senha;
}
