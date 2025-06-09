package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.eventos_usuario.EventoUsuario;
import com.api.jesus_king_tech.domain.eventos_usuario.repository.EventoUsuarioRepository;
import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.domain.postagem.repository.PostagemRepository;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoUsuarioService {

    @Autowired
    private EventoUsuarioRepository eventoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostagemRepository postagemRepository;

    // 1. Registrar que o usuário curtiu um evento
    public void registrarCurtida(Integer usuarioId, Integer postagemId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        EventoUsuario eventoUsuario = eventoUsuarioRepository
                .findByUsuarioIdAndPostagemId(usuarioId, postagemId)
                .orElse(new EventoUsuario());

        eventoUsuario.setUsuario(usuario);
        eventoUsuario.setPostagem(postagem);
        eventoUsuario.setCurtiu(true);

        eventoUsuarioRepository.save(eventoUsuario);
    }

    // 2. Registrar que o usuário confirmou presença no evento
    public void registrarPresenca(Integer usuarioId, Integer postagemId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        EventoUsuario eventoUsuario = eventoUsuarioRepository
                .findByUsuarioIdAndPostagemId(usuarioId, postagemId)
                .orElse(new EventoUsuario());

        eventoUsuario.setUsuario(usuario);
        eventoUsuario.setPostagem(postagem);
        eventoUsuario.setConfirmouPresenca(true);

        eventoUsuarioRepository.save(eventoUsuario);
    }

    // 3. Mudar registro que o usuário deixou de curtir o evento
    public void removerCurtida(Integer usuarioId, Integer postagemId) {
        EventoUsuario eventoUsuario = eventoUsuarioRepository
                .findByUsuarioIdAndPostagemId(usuarioId, postagemId)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        eventoUsuario.setCurtiu(false);
        eventoUsuarioRepository.save(eventoUsuario);
    }

    // 4. Mudar registro que o usuário cancelou a presença no evento
    public void cancelarPresenca(Integer usuarioId, Integer postagemId) {
        EventoUsuario eventoUsuario = eventoUsuarioRepository
                .findByUsuarioIdAndPostagemId(usuarioId, postagemId)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        eventoUsuario.setConfirmouPresenca(false);
        eventoUsuarioRepository.save(eventoUsuario);
    }

    // 5. Contar quantos usuários estão confirmados para um determinado evento
    public long contarConfirmacoesPresenca(Integer postagemId) {
        return eventoUsuarioRepository.countByPostagemIdAndConfirmouPresencaTrue(postagemId);
    }

    public List<Postagem> listarEventosConfirmados(Integer usuarioId) {
        return eventoUsuarioRepository.findAll().stream()
                .filter(eventoUsuario -> eventoUsuario.getUsuario().getId().equals(usuarioId) && eventoUsuario.isConfirmouPresenca())
                .map(EventoUsuario::getPostagem)
                .toList();
    }

    public List<Postagem> listarEventosCurtidos(Integer usuarioId) {
        return eventoUsuarioRepository.findAll().stream()
                .filter(eventoUsuario -> eventoUsuario.getUsuario().getId().equals(usuarioId) && eventoUsuario.isCurtiu())
                .map(EventoUsuario::getPostagem)
                .toList();
    }
}
