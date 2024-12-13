package com.api.jesus_king_tech.domain.endereco;



import com.api.jesus_king_tech.domain.cesta_basica.CestaBasica;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

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

    @NotBlank(message = "O campo CEP é obrigatório.")
    private String cep;

    @NotBlank(message = "O campo logradouro é obrigatório.")
    private String logradouro;

    @NotBlank(message = "O campo número é obrigatório")
    private String numero;

    private String complemento;

    @NotBlank(message = "O campo bairro é obrigatório.")
    private String bairro;

    @NotBlank(message = "O campo localidade é obrigatório.")
    private String localidade;

    @NotBlank(message = "O campo UF é obrigatório.")
    @Pattern(regexp = "[A-Z]{2}", message = "UF deve ser um código de 2 letras.")
    private String uf;

    @OneToMany(mappedBy = "endereco")
    private List<CestaBasica> cestasBasicas;



}
