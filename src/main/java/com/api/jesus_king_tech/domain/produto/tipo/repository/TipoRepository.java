package com.api.jesus_king_tech.domain.produto.tipo.repository;

import com.api.jesus_king_tech.domain.produto.tipo.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TipoRepository extends JpaRepository<Tipo, Integer> {


    Collection<Tipo> findByCategoriaId(Integer id);
}