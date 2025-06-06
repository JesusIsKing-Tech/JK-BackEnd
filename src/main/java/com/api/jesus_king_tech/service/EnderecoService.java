package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.endereco.dto.*;
import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.endereco.repository.EnderecoRepository;
import com.api.jesus_king_tech.domain.endereco.repository.ViaCepClient;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViaCepClient viaCepClient;

    @Autowired
    private UsuarioRepository usuarioRepository;



    private final ListaEstaticaEnderecoResponse listaEnderecos;

    public EnderecoService() {
        this.listaEnderecos = new ListaEstaticaEnderecoResponse(100);
    }

    public ListaEstaticaEnderecoResponse getListaEnderecos() {
        return listaEnderecos;
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



    public EnderecoViaCepDTO buscarEnderecoPorCep(String cep) {
        EnderecoDTO enderecoDTO = viaCepClient.buscarEnderecoPorCep(cep);

        if (enderecoDTO == null || enderecoDTO.getLogradouro() == null) {
            throw new IllegalArgumentException("Endereço não encontrado para o CEP: " + cep);
        }

        return EnderecoMapper.toViaCepDto(EnderecoMapper.toEntity(enderecoDTO));
    }



    public List<EnderecoResponse> getEnderecosOrdenados() {
        List<EnderecoResponse> enderecos = listaEnderecos.exibir();
        ListaEstaticaEnderecoResponse.quickSort(enderecos, 0, enderecos.size() - 1);
        return enderecos;
    }

    public Endereco enderecoExiste(Endereco enderecoCriarDto) {
        Optional<Endereco> enderecoExistente =
                enderecoRepository.findByCepAndLogradouroAndBairroAndLocalidadeAndUfAndNumeroAndComplemento(
                enderecoCriarDto.getCep(),
                enderecoCriarDto.getLogradouro(),
                enderecoCriarDto.getBairro(),
                enderecoCriarDto.getLocalidade(),
                enderecoCriarDto.getUf(),
                enderecoCriarDto.getNumero(),
                enderecoCriarDto.getComplemento()
        );

        return enderecoExistente.orElse(null);

    }

    public Integer verificarEndereco(EnderecoDTO enderecoDTO) throws ClassNotFoundException {
        Optional<Endereco> enderecoOpt = enderecoRepository.findByCepAndLogradouroAndBairroAndLocalidadeAndUfAndNumeroAndComplemento(
                enderecoDTO.getCep(), enderecoDTO.getLogradouro(), enderecoDTO.getBairro(),enderecoDTO.getLocalidade(),enderecoDTO.getUf(),enderecoDTO.getNumero(), enderecoDTO.getComplemento());
        if (enderecoOpt.isPresent()) {
            Endereco endereco = enderecoOpt.get();
            if (usuarioRepository.existsByEndereco(endereco)){
                return endereco.getId();
            }
        }
        throw new ClassNotFoundException("Endereço não encontrado");
    }
}








