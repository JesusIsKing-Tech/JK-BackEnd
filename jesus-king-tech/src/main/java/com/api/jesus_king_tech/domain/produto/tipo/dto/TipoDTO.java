package com.api.jesus_king_tech.domain.produto.tipo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TipoDTO {

    private Integer id;
    private String nome;

}
