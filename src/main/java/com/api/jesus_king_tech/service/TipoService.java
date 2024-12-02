package com.api.jesus_king_tech.service;


import com.api.jesus_king_tech.domain.produto.dto.ProdutoMapper;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.domain.produto.tipo.dto.TipoDTO;
import com.api.jesus_king_tech.domain.produto.tipo.repository.TipoRepository;
import com.api.jesus_king_tech.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoService {


    @Autowired
    private TipoRepository tipoRepository;

    private final ProdutoMapper produtoMapper = new ProdutoMapper();


    public TipoDTO cadastrarTipo(Tipo tipo) {
        return produtoMapper.toTipoDTO(tipoRepository.save(tipo));
    }

    public TipoDTO atualizarTipo(Integer id, Tipo tipoAtualizado) {
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo não encontrado"));

        tipo.setNome(tipoAtualizado.getNome());
        return produtoMapper.toTipoDTO(tipoRepository.save(tipo));
    }

    public void removerTipo(Integer id) {
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo não encontrado"));

        tipoRepository.delete(tipo);
    }

    public List<TipoDTO> listarTodosTipos() {
        return tipoRepository.findAll().stream()
                .map(produtoMapper::toTipoDTO)
                .collect(Collectors.toList());
    }

}
