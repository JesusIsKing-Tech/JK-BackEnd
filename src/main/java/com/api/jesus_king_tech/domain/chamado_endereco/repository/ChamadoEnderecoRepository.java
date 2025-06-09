package com.api.jesus_king_tech.domain.chamado_endereco.repository;

import com.api.jesus_king_tech.domain.chamado_endereco.ChamadoEndereco;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChamadoEnderecoRepository extends JpaRepository<ChamadoEndereco, Integer> {

    Optional<ChamadoEndereco> findByUsuarioAndStatus(Usuario usuario, ChamadoEndereco.StatusChamado statusChamado);

    List<ChamadoEndereco> findAllByStatus(ChamadoEndereco.StatusChamado statusChamado);

    Optional<ChamadoEndereco> findByIdAndStatus(Integer chamadoId, ChamadoEndereco.StatusChamado statusChamado);
}
