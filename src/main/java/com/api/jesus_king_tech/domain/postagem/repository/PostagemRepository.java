package com.api.jesus_king_tech.domain.postagem.repository;

import com.api.jesus_king_tech.domain.postagem.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Integer> {

    List<Postagem> findByDataBetween(LocalDate today, LocalDate nextWeek);
}
