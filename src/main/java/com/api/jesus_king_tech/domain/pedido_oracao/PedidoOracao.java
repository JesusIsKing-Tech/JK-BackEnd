package com.api.jesus_king_tech.domain.pedido_oracao;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoOracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O campo descrição é obrigatório.")
    private String descricao;

    @ManyToOne
    @NotNull(message = "O campo idUsuario é obrigatório.")
    private Usuario usuario;

    @Column(nullable = false)
    private boolean orado = false; // Indica se o pedido de oração já foi orado ou não
}

