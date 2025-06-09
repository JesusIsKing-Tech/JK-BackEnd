package com.api.jesus_king_tech.domain.postagem.dto;

import com.api.jesus_king_tech.domain.postagem.Postagem;

import java.util.List;
import java.util.stream.Collectors;

public class PostagemMapper {

    public static PostagemDto toDto(Postagem postagem) {
        return PostagemDto.builder()
                .id(postagem.getId())
                .titulo(postagem.getTitulo())
                .data(postagem.getData())
                .descricao(postagem.getDescricao())
                .valor(postagem.getValor())
                .horaEvento(postagem.getHoraEvento())
                .endereco(postagem.getEndereco())
                .build();
    }

    public static List<PostagemDto> toDtoList(List<Postagem> postagens) {
        return postagens.stream()
                .map(PostagemMapper::toDto)
                .collect(Collectors.toList());
    }
}
