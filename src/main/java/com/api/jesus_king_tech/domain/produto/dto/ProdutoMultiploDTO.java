package com.api.jesus_king_tech.domain.produto.dto;

import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;
import com.api.jesus_king_tech.domain.produto.tipo.dto.TipoDTO;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoMultiploDTO {

    private Integer id;
    private LocalDate dataEntrada = LocalDate.now();
    private Double peso_litro;
    private CategoriaDTO categoria;
    private TipoDTO tipo;
    private Integer quantidade;
}
