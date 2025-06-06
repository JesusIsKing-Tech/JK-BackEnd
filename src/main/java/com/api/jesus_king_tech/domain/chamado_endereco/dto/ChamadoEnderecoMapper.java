package com.api.jesus_king_tech.domain.chamado_endereco.dto;

import com.api.jesus_king_tech.domain.chamado_endereco.ChamadoEndereco;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;

public class ChamadoEnderecoMapper {

    public static ChamadoEnderecoDto toDto(ChamadoEndereco chamadoEndereco) {
        return ChamadoEnderecoDto.builder()
                .id(chamadoEndereco.getId())
                .cep(chamadoEndereco.getCep())
                .logradouro(chamadoEndereco.getLogradouro())
                .numero(chamadoEndereco.getNumero())
                .complemento(chamadoEndereco.getComplemento())
                .bairro(chamadoEndereco.getBairro())
                .localidade(chamadoEndereco.getLocalidade())
                .uf(chamadoEndereco.getUf())
                .usuario(UsuarioResponseDto.builder()
                        .nome(chamadoEndereco.getUsuario().getNome())
                        .email(chamadoEndereco.getUsuario().getEmail())
                        .telefone(chamadoEndereco.getUsuario().getTelefone())
                        .data_nascimento(chamadoEndereco.getUsuario().getData_nascimento())
                        .genero(chamadoEndereco.getUsuario().getGenero())
                        .foto_perfil_url(chamadoEndereco.getUsuario().getFotoPerfilUrl())
                        .endereco(chamadoEndereco.getUsuario().getEndereco())
                        .build())
                .status(chamadoEndereco.getStatus())
                .createdAt(chamadoEndereco.getCreated_at())
                .updatedAt(chamadoEndereco.getUpdated_at())
                .build();
    }
}
