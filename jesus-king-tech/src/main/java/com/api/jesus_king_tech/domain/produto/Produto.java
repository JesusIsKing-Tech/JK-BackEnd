package com.api.jesus_king_tech.domain.produto;

import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double peso;

    @Column(name = "dtEntrada")
    private LocalDate dataEntrada = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "fk_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "fk_tipo", nullable = false)
    private Tipo tipo;

}