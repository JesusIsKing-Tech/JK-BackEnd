package com.api.jesus_king_tech.domain.pedido_oracao.dto;

import com.api.jesus_king_tech.domain.pedido_oracao.PedidoOracao;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMapper;

public class PedidoOracaoMapper {

    private UsuarioMapper usuarioMapper;

    public static PedidoOracaoResponseDTO toResponseDTO(PedidoOracao pedidoOracao) {
        return PedidoOracaoResponseDTO.builder()
                .id(pedidoOracao.getId())
                .descricao(pedidoOracao.getDescricao())
                .usuario(UsuarioMapper.usuarioEntityToDto(pedidoOracao.getUsuario()))
                .build();
    }
}
