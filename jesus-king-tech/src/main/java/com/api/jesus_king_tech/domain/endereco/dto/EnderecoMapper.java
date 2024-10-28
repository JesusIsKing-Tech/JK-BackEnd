package com.api.jesus_king_tech.domain.endereco.dto;


import com.api.jesus_king_tech.domain.endereco.Endereco;

public class EnderecoMapper {


    public static Endereco toEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }

        return Endereco.builder()
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .localidade(dto.getLocalidade())
                .uf(dto.getUf())
                .build();
    }

    public static EnderecoDTO toDto(Endereco entity) {
        if (entity == null) {
            return null;
        }

        return EnderecoDTO.builder()
                .cep(entity.getCep())
                .logradouro(entity.getLogradouro())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .localidade(entity.getLocalidade())
                .uf(entity.getUf())
                .build();
    }

    // Corrigido para receber Endereco em vez de EnderecoDTO
    public static EnderecoResponse toResponse(Endereco entity) {
        if (entity == null) {
            return null; // Retorna null se o objeto Endereco for null
        }

        return EnderecoResponse.builder()
                .cep(entity.getCep())
                .logradouro(entity.getLogradouro())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .localidade(entity.getLocalidade())
                .uf(entity.getUf())
                .build();
    }
}



