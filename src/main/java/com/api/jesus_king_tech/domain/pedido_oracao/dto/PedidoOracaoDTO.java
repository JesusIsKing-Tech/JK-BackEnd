package com.api.jesus_king_tech.domain.pedido_oracao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class PedidoOracaoDTO {

    @NotBlank(message = "O campo descrição é obrigatório.")
    private String descricao;

    @NotNull(message = "O campo idUsuario é obrigatório.")
    private Integer idUsuario;
}