package com.api.jesus_king_tech.domain.usuario.dto;

import com.api.jesus_king_tech.domain.usuario.Usuario;

public class UsuarioMapper {

    public static Usuario usuarioDtoToEntity(UsuarioCriarDto dto){
        if (dto == null){return null;}

        return Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .telefone(dto.getTelefone())
                .data_nascimento(dto.getData_nascimento())
                .genero(dto.getGenero())
                .build();
    }

    public static  UsuarioResponseDto usuarioEntityToDto(Usuario entity) {
        if (entity == null) {
            return null;
        }

        return UsuarioResponseDto.builder()
                .nome(entity.getNome())
                .email(entity.getEmail())
                .telefone(entity.getTelefone())
                .data_nascimento(entity.getData_nascimento())
                .genero(entity.getGenero())
                .build();

    }

}