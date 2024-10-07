package com.api.jesus_king_tech.endereco.service;

import com.api.jesus_king_tech.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.endereco.entity.Endereco;
import com.api.jesus_king_tech.endereco.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> findById(Integer id) {
        return enderecoRepository.findById(id);
    }

    public Endereco save(EnderecoDTO enderecoDTO) {
        Endereco endereco = Endereco.builder()
                .cep(enderecoDTO.getCep())
                .logradouro(enderecoDTO.getLogradouro())
                .numero(enderecoDTO.getNumero())
                .bairro(enderecoDTO.getBairro())
                .localidade(enderecoDTO.getLocalidade())
                .uf(enderecoDTO.getUf())
                .build();
        return enderecoRepository.save(endereco);
    }

    public void delete(Integer id) {
        enderecoRepository.deleteById(id);
    }
}