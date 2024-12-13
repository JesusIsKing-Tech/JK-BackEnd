package com.api.jesus_king_tech.service;


import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import com.api.jesus_king_tech.domain.produto.categoria.dto.CategoriaDTO;
import com.api.jesus_king_tech.domain.produto.categoria.repository.CategoriaRepository;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoMapper;
import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import com.api.jesus_king_tech.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {


    @Autowired
    private CategoriaRepository categoriaRepository;


    private final ProdutoMapper produtoMapper = new ProdutoMapper();

    public CategoriaDTO cadastrarCategoria(Categoria categoria) {
        for (Tipo tipo : categoria.getTipo()){
            tipo.setCategoria(categoria);
        }

        Categoria savedCategoria = categoriaRepository.save(categoria);

        return produtoMapper.toCategoriaDTO(savedCategoria);
    }

    public CategoriaDTO atualizarCategoria(Integer id, Categoria categoriaAtualizada) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        categoria.setNome(categoriaAtualizada.getNome());
        return produtoMapper.toCategoriaDTO(categoriaRepository.save(categoria));
    }

    public void removerCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        categoriaRepository.delete(categoria);
    }

    public List<CategoriaDTO> listarTodasCategorias() {
        return categoriaRepository.findAll().stream()
                .map(produtoMapper::toCategoriaDTO)
                .collect(Collectors.toList());
    }


}