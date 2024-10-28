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
                .bairro(entity.getBairro())
                .localidade(entity.getLocalidade())
                .uf(entity.getUf())
                .build();
    }

    // Mapeamento manual de Entidade para Response
    public static EnderecoResponse toResponse(Endereco entity) {
        if (entity == null) {
            return null;
        }

        return EnderecoResponse.builder()
                .cep(entity.getCep())
                .logradouro(entity.getLogradouro())
                .bairro(entity.getBairro())
                .localidade(entity.getLocalidade())
                .uf(entity.getUf())
                .build();
    }
}



