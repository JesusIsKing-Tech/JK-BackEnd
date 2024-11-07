package com.api.jesus_king_tech.domain.produto.categoria.repository;

import com.api.jesus_king_tech.domain.produto.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
