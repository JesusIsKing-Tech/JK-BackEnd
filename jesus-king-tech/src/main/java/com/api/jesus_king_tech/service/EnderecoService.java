package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoMapper;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoResponse;
import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.endereco.repository.EnderecoRepository;
import com.api.jesus_king_tech.domain.endereco.repository.ViaCepClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepClient viaCepClient;



    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Optional<EnderecoResponse> findById(Integer id) {
        Optional<Endereco> endereco = enderecoRepository.findById(id);
        return endereco.map(EnderecoMapper::toResponse);
    }

    public EnderecoResponse save(EnderecoDTO enderecoDTO) {
        EnderecoDTO viaCepData = viaCepClient.buscarEnderecoPorCep(enderecoDTO.getCep());
        if (viaCepData != null) {
            Endereco endereco = EnderecoMapper.toEntity(viaCepData);
            enderecoRepository.save(endereco);
            return EnderecoMapper.toResponse(endereco);
        }
        return null;
    }

    public boolean existsById(Integer id) {
        return enderecoRepository.existsById(id);
    }

    public void delete(Integer id) {
        enderecoRepository.deleteById(id);
    }


    public EnderecoResponse buscarEnderecoPorCep(String cep) {
        EnderecoDTO enderecoDTO = viaCepClient.buscarEnderecoPorCep(cep);
        Endereco endereco = EnderecoMapper.toEntity(enderecoDTO);
        enderecoRepository.save(endereco);
        return EnderecoMapper.toResponse(endereco);
    }


    public List<EnderecoResponse> getEnderecosOrdenados() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos.stream()
                .sorted(Comparator.comparing(Endereco::getLogradouro))
                .map(EnderecoMapper::toResponse)
                .collect(Collectors.toList());
    }

        }





