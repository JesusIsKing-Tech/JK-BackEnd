package com.api.jesus_king_tech.endereco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoDTO {

    private Integer id;

    @NotBlank(message = "O CEP não pode estar vazio.")
    @Size(min = 8, max = 8, message = "O CEP deve ter 8 caracteres.")
    private String cep;

    @NotBlank(message = "O logradouro não pode estar vazio.")
    private String logradouro;

    @NotBlank(message = "O número não pode estar vazio.")
    private String numero;

    @NotBlank(message = "O bairro não pode estar vazio.")
    private String bairro;

    @NotBlank(message = "A localidade não pode estar vazia.")
    private String localidade;

    @NotBlank(message = "A UF não pode estar vazia.")
    @Size(min = 2, max = 2, message = "A UF deve ter 2 caracteres.")
    private String uf;
}