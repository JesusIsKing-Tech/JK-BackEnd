package com.api.jesus_king_tech.domain.produto.repository;

import com.api.jesus_king_tech.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAllByCategoriaNome(String nomeProduto);
}
