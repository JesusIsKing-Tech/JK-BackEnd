package com.api.jesus_king_tech.domain.usuario.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioAtualizarSimplesDto {

    private String nome;
    private String email;
    private String telefone;
    private boolean receber_doacoes;


}
