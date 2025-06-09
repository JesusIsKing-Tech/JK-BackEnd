package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.pedido_oracao.PedidoOracao;
import com.api.jesus_king_tech.domain.pedido_oracao.dto.PedidoOracaoDTO;
import com.api.jesus_king_tech.domain.pedido_oracao.dto.PedidoOracaoResponseDTO;
import com.api.jesus_king_tech.service.PedidoOracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos-oracao")
public class PedidoOracaoController {

    @Autowired
    private PedidoOracaoService pedidoOracaoService;

    @GetMapping
    public ResponseEntity<List<PedidoOracaoResponseDTO>> listarPedidosOracao() {
        List<PedidoOracaoResponseDTO> listagem = pedidoOracaoService.listarPedidosOracao();

        if (listagem.isEmpty()){
            ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok(listagem);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<PedidoOracaoResponseDTO> cadastrarPedidoOracao(@RequestBody PedidoOracaoDTO novoPedidoOracao) {
        PedidoOracaoResponseDTO pedidoOracaoSalvo = pedidoOracaoService.criarPedidoOracao(novoPedidoOracao);

        return ResponseEntity.status(201).body(pedidoOracaoSalvo);
    }

    @PostMapping("/{id}/orar")
    public ResponseEntity<Void> orarPorPedidoOracao(@PathVariable Integer id) {
        pedidoOracaoService.orarPorPedidoOracao(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedidoOracao(@PathVariable Integer id) {
        pedidoOracaoService.deletarPedidoOracao(id);

        return ResponseEntity.noContent().build();
    }
}
