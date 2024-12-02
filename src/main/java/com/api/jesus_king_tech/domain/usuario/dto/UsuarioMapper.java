package com.api.jesus_king_tech.domain.usuario.dto;

import com.api.jesus_king_tech.domain.endereco.dto.EnderecoMapper;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioTokenDto;

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
                .receber_doacoes(dto.isReceber_doacoes())
                .endereco(dto.getEndereco())
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
                .endereco(entity.getEndereco())
                .build();

    }

    public static Usuario usuarioAtualizarToUsuarioEntity(UsuarioAtualizarDto usuarioAtualizar){
        if (usuarioAtualizar == null){
            return null;
        }

        return Usuario.builder()
                .nome(usuarioAtualizar.getNome())
                .email(usuarioAtualizar.getEmail())
                .senha(usuarioAtualizar.getSenha())
                .telefone(usuarioAtualizar.getTelefone())
                .data_nascimento(usuarioAtualizar.getData_nascimento())
                .genero(usuarioAtualizar.getGenero())
                .build();
    }

    public static UsuarioTokenDto of(Usuario usuario, String token){
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }


}
