package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.service.EventoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento-usuario")
public class EventoUsuarioController {

    @Autowired
    private EventoUsuarioService eventoUsuarioService;

    // Endpoint para registrar curtida
    @PostMapping("/curtir")
    public void registrarCurtida(@RequestParam Integer usuarioId, @RequestParam Integer postagemId) {
        eventoUsuarioService.registrarCurtida(usuarioId, postagemId);
    }

    // Endpoint para registrar presença
    @PostMapping("/presenca")
    public void registrarPresenca(@RequestParam Integer usuarioId, @RequestParam Integer postagemId) {
        eventoUsuarioService.registrarPresenca(usuarioId, postagemId);
    }

    // Endpoint para remover curtida
    @PutMapping("/remover-curtida")
    public void removerCurtida(@RequestParam Integer usuarioId, @RequestParam Integer postagemId) {
        eventoUsuarioService.removerCurtida(usuarioId, postagemId);
    }

    // Endpoint para cancelar presença
    @PutMapping("/cancelar-presenca")
    public void cancelarPresenca(@RequestParam Integer usuarioId, @RequestParam Integer postagemId) {
        eventoUsuarioService.cancelarPresenca(usuarioId, postagemId);
    }

    // Endpoint para contar confirmações de presença
    @GetMapping("/contar-presencas/{postagemId}")
    public long contarConfirmacoesPresenca(@PathVariable Integer postagemId) {
        return eventoUsuarioService.contarConfirmacoesPresenca(postagemId);
    }

    @GetMapping("/eventos-confirmado/{usuarioId}")
    public List<Postagem> contarEventosConfirmados(@PathVariable Integer usuarioId) {
        return eventoUsuarioService.listarEventosConfirmados(usuarioId);
    }

    @GetMapping("/eventos-curtidos/{usuarioId}")
    public List<Postagem> listarEventosCurtidos(@PathVariable Integer usuarioId) {
        return eventoUsuarioService.listarEventosCurtidos(usuarioId);
    }
}
