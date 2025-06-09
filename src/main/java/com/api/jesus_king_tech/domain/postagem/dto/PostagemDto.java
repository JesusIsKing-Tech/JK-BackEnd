package com.api.jesus_king_tech.domain.postagem.dto;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostagemDto {

    private Integer id;
    private String titulo;
    private LocalDate data;
    private String descricao;
    private Double valor;
    private LocalTime horaEvento;
    private Endereco endereco;
}
