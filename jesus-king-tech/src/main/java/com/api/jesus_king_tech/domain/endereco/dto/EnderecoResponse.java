package com.api.jesus_king_tech.domain.endereco.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoResponse {


    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;


}
