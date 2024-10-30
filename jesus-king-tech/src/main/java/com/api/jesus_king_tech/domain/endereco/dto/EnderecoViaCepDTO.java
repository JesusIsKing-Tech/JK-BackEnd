package com.api.jesus_king_tech.domain.endereco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoViaCepDTO {

    
    private String cep;
   
    private String logradouro;
  
    private String bairro;
  
    private String localidade;
    
    private String uf;



}
