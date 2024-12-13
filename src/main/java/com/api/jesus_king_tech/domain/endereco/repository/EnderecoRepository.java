package com.api.jesus_king_tech.domain.endereco.repository;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

    Optional<Endereco> findByCepAndLogradouroAndBairroAndLocalidadeAndUfAndNumeroAndComplemento(
            String cep, String logradouro, String bairro, String localidade, String uf, String numero, String complemento);

}