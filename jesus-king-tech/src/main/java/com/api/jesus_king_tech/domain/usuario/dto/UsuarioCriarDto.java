package com.api.jesus_king_tech.domain.usuario.dto;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UsuarioCriarDto {


    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate data_nascimento;
    private String genero;
    private boolean receber_doacoes;

    private Endereco endereco;

}
