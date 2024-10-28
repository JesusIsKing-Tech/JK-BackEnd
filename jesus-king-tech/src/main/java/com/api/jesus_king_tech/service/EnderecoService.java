package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoMapper;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoResponse;
import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.endereco.dto.ListaEstaticaEnderecoResponse;
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



    private final ListaEstaticaEnderecoResponse listaEnderecos;

    public EnderecoService() {
        this.listaEnderecos = new ListaEstaticaEnderecoResponse(100);
    }

    public EnderecoResponse save(EnderecoDTO enderecoDTO) {
        Endereco endereco = EnderecoMapper.toEntity(enderecoDTO);
        enderecoRepository.save(endereco);
        EnderecoResponse enderecoResponse = EnderecoMapper.toResponse(endereco);
        listaEnderecos.adiciona(enderecoResponse);

        return enderecoResponse;
    }


    public Optional<EnderecoResponse> findById(int id) {
        return listaEnderecos.findById(id);
    }

    public boolean existsById(int id) {
        return listaEnderecos.findById(id).isPresent();
    }

    public void delete(int id) {
        listaEnderecos.removePeloIndice(id);
    }



    public EnderecoResponse buscarEnderecoPorCep(String cep) {
        EnderecoDTO enderecoDTO = viaCepClient.buscarEnderecoPorCep(cep);

        if (enderecoDTO == null || enderecoDTO.getLogradouro() == null) {
            throw new IllegalArgumentException("Endereço não encontrado para o CEP: " + cep);
        }

        return EnderecoMapper.toResponse(EnderecoMapper.toEntity(enderecoDTO));
    }



    public List<EnderecoResponse> getEnderecosOrdenados() {
        List<EnderecoResponse> enderecos = listaEnderecos.toList();

        int n = enderecos.size();
        for (int i = 0; i < n - 1; i++) {
            int indexMenor = i;
            for (int j = i + 1; j < n; j++) {
                if (enderecos.get(j).getLogradouro().compareTo(enderecos.get(indexMenor).getLogradouro()) < 0) {
                    indexMenor = j;
                }
                EnderecoResponse temp = enderecos.get(indexMenor);
                enderecos.set(indexMenor, enderecos.get(i));
                enderecos.set(i, temp);
            }

        }
        return enderecos;
    }

}








