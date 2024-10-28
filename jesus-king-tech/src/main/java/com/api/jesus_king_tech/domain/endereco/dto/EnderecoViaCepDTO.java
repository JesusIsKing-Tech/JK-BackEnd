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

    @NotBlank(message = "O campo CEP é obrigatório.")
    private String cep;
    @NotBlank(message = "O campo logradouro é obrigatório.")
    private String logradouro;
    @NotBlank(message = "O campo bairro é obrigatório.")
    private String bairro;
    @NotBlank(message = "O campo localidade é obrigatório.")
    private String localidade;
    @NotBlank(message = "O campo UF é obrigatório.")
    @Pattern(regexp = "[A-Z]{2}", message = "UF deve ser um código de 2 letras.")
    private String uf;



}
