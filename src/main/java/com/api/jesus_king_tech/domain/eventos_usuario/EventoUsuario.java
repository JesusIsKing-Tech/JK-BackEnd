package com.api.jesus_king_tech.domain.eventos_usuario;

import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "postagem_id", nullable = false)
    private Postagem postagem;

    @Column(nullable = false)
    private boolean curtiu;

    @Column(nullable = false)
    private boolean confirmouPresenca;
}
