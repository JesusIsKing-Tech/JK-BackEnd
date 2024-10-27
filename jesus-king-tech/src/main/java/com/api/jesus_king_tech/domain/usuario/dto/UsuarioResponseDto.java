package com.api.jesus_king_tech.domain.usuario.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UsuarioResponseDto {

    private String nome;
    private String email;
    private String telefone;
    private LocalDate data_nascimento;
    private String genero;
}
