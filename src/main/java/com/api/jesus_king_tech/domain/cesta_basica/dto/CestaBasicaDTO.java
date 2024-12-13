package com.api.jesus_king_tech.domain.cesta_basica.dto;

import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CestaBasicaDTO {

    private Integer id;
    private LocalDate dataMontagem;
    private LocalDate dataRetirada;
    private List<ProdutoDTO> produtos;
}
