package com.api.jesus_king_tech.domain.cesta_basica;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CestaBasica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataMontagem = LocalDate.now();
    private LocalDate dataRetirada;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = true)
    private Endereco endereco;

    @OneToMany(mappedBy = "cestaBasica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;


}
