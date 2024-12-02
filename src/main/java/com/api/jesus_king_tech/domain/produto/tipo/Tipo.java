package com.api.jesus_king_tech.domain.produto.tipo;

import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipos")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;



}