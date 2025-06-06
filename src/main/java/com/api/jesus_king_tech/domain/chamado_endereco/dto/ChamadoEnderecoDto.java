package com.api.jesus_king_tech.domain.chamado_endereco.dto;

import com.api.jesus_king_tech.domain.chamado_endereco.ChamadoEndereco;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChamadoEnderecoDto {

    private Integer id;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private UsuarioResponseDto usuario;
    private ChamadoEndereco.StatusChamado status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
