package com.api.jesus_king_tech.endereco.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
