package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.pedido_oracao.PedidoOracao;
import com.api.jesus_king_tech.domain.pedido_oracao.dto.PedidoOracaoDTO;
import com.api.jesus_king_tech.domain.pedido_oracao.dto.PedidoOracaoMapper;
import com.api.jesus_king_tech.domain.pedido_oracao.dto.PedidoOracaoResponseDTO;
import com.api.jesus_king_tech.domain.pedido_oracao.repository.PedidoOracaoRepository;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoOracaoService {

    @Autowired
    private PedidoOracaoRepository pedidoOracaoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<PedidoOracaoResponseDTO> listarPedidosOracao() {
               List<PedidoOracao> pedidos = pedidoOracaoRepository.findAll();
               List<PedidoOracaoResponseDTO> pedidosResponse = new ArrayList<>();

               for (PedidoOracao pedido : pedidos) {
                   PedidoOracaoResponseDTO pedidoResponse = PedidoOracaoMapper.toResponseDTO(pedido);
                   pedidosResponse.add(pedidoResponse);
               }
        return pedidosResponse;
    }

    public PedidoOracaoResponseDTO criarPedidoOracao(PedidoOracaoDTO novoPedidoOracaoDTO) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(novoPedidoOracaoDTO.getIdUsuario());

        PedidoOracao pedidoOracao = PedidoOracao.builder()
                .descricao(novoPedidoOracaoDTO.getDescricao())
                .usuario(usuario)
                .build();

        PedidoOracao pedidoSalvo = pedidoOracaoRepository.save(pedidoOracao);

        PedidoOracaoResponseDTO pedidoResponse = PedidoOracaoResponseDTO.builder()
                .id(pedidoSalvo.getId())
                .descricao(pedidoSalvo.getDescricao())
                .usuario(UsuarioMapper.usuarioEntityToDto(pedidoSalvo.getUsuario()))
                .build();

        return pedidoResponse;
    }

    public void deletarPedidoOracao(Integer id) {
        pedidoOracaoRepository.deleteById(id);
    }
}
