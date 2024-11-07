package com.api.jesus_king_tech.domain.usuario;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O campo \"Nome\" não pode estar em branco")
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate data_nascimento;
    private String genero;
    private boolean receber_doacoes;
    private String codigo_recuperar_senha;
    private LocalDateTime validade_codigo_senha;

    @ManyToOne
    private Endereco endereco;

}