package com.api.jesus_king_tech.domain.eventos_usuario.repository;

import com.api.jesus_king_tech.domain.eventos_usuario.EventoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventoUsuarioRepository extends JpaRepository<EventoUsuario, Integer> {
    // Aqui você pode adicionar métodos personalizados, se necessário
    // Exemplo: List<EventoUsuario> findByEventoId(Integer eventoId);

    Optional<EventoUsuario> findByUsuarioIdAndPostagemId(Integer usuarioId, Integer postagemId);

    long countByPostagemIdAndConfirmouPresencaTrue(Integer postagemId);
}
