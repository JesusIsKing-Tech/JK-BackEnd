package com.api.jesus_king_tech.domain.pedido_oracao.dto;

import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoOracaoResponseDTO {

        private Integer id;

        private String descricao;

        private UsuarioResponseDto usuario;
}
