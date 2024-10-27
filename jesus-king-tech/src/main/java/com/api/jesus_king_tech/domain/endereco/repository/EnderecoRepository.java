package com.api.jesus_king_tech.domain.endereco.repository;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}