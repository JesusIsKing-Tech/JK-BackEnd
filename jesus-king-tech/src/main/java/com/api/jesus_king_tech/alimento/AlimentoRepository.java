package com.api.jesus_king_tech.alimento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    Optional<Alimento> findByNome(String nome);


    Optional<Alimento> findById(Integer id);
}
