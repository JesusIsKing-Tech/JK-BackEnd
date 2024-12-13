package com.api.jesus_king_tech.domain.produto.categoria.dto;

import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {

    private Integer id;
    private String nome;
}

