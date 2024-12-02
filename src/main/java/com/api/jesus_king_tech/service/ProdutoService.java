package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.produto.Produto;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoDTO;
import com.api.jesus_king_tech.domain.produto.dto.ProdutoMapper;
import com.api.jesus_king_tech.domain.produto.repository.ProdutoRepository;
import com.api.jesus_king_tech.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;

    private final ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = new ProdutoMapper();
    }


    public ProdutoDTO cadastrarProduto(Produto produto) {
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toProdutoDTO(produtoSalvo);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public ProdutoDTO atualizarProduto(Integer id, Produto produtoAtualizado) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setPeso(produtoAtualizado.getPeso());
            produto.setCategoria(produtoAtualizado.getCategoria());
            produto.setTipo(produtoAtualizado.getTipo());
            produtoRepository.save(produto);
            return produtoMapper.toProdutoDTO(produto);
        }
        return null;
    }

    public boolean deletarProduto(Integer id) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isPresent()) {
            produtoRepository.delete(produtoOpt.get());
            return true;
        }
        return false;
    }
}