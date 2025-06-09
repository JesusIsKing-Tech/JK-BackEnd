package com.api.jesus_king_tech.domain.postagem;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String titulo;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate data;

    private String descricao;

    private Double valor;

    @Lob
    @Column(name = "imagem", columnDefinition = "LONGBLOB")
    private byte[] imagem;

    @Column(name = "imagem_mime_type")
    private String imagemMimeType;

    @Column(name = "hora_evento")
    private LocalTime horaEvento;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
