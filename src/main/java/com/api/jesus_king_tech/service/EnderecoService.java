package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.chamado_endereco.ChamadoEndereco;
import com.api.jesus_king_tech.domain.chamado_endereco.dto.ChamadoEnderecoDto;
import com.api.jesus_king_tech.domain.chamado_endereco.dto.ChamadoEnderecoMapper;
import com.api.jesus_king_tech.domain.chamado_endereco.repository.ChamadoEnderecoRepository;
import com.api.jesus_king_tech.domain.endereco.dto.*;
import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.endereco.repository.EnderecoRepository;
import com.api.jesus_king_tech.domain.endereco.repository.ViaCepClient;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private ChamadoEnderecoRepository chamadoEnderecoRepository;



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

    public ChamadoEnderecoDto abrirChamado(EnderecoDTO enderecoDTO, Integer userId) {

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID: " + userId));

        boolean chamadoAberto = chamadoEnderecoRepository.findByUsuarioAndStatus(usuario, ChamadoEndereco.StatusChamado.ABERTO)
                .isPresent();

        if (chamadoAberto) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um chamado aberto para o usuário com ID: " + userId);
        }

        ChamadoEndereco chamado = ChamadoEndereco.builder()
                .cep(enderecoDTO.getCep())
                .logradouro(enderecoDTO.getLogradouro())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .bairro(enderecoDTO.getBairro())
                .localidade(enderecoDTO.getLocalidade())
                .uf(enderecoDTO.getUf())
                .usuario(usuario)
                .status(ChamadoEndereco.StatusChamado.ABERTO) // Status inicial
                .build();

        chamadoEnderecoRepository.save(chamado);

        return ChamadoEnderecoMapper.toDto(chamado);
    }


    public void aprovarChamado(Integer chamadoId) {
        ChamadoEndereco chamado = chamadoEnderecoRepository.findByIdAndStatus(chamadoId, ChamadoEndereco.StatusChamado.ABERTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum chamado aberto encontrado para o com ID: " + chamadoId));

        Usuario usuario = chamado.getUsuario();

       Endereco enderecoNovo = enderecoRepository.findByCepAndLogradouroAndBairroAndLocalidadeAndUfAndNumeroAndComplemento(
                chamado.getCep(),
                chamado.getLogradouro(),
                chamado.getBairro(),
                chamado.getLocalidade(),
                chamado.getUf(),
                chamado.getNumero(),
                chamado.getComplemento()
        ).orElse(null);

       if (enderecoNovo == null) {
           Endereco endereco = enderecoRepository.save(Endereco.builder()
                   .cep(chamado.getCep())
                   .logradouro(chamado.getLogradouro())
                   .numero(chamado.getNumero())
                   .complemento(chamado.getComplemento())
                   .bairro(chamado.getBairro())
                   .localidade(chamado.getLocalidade())
                   .uf(chamado.getUf())
                   .build());

              usuario.setEndereco(endereco);
        }else {
           usuario.setEndereco(enderecoNovo);
         }

       chamado.setStatus(ChamadoEndereco.StatusChamado.APROVADO);
       chamado.setUpdated_at(java.time.LocalDateTime.now());

       chamadoEnderecoRepository.save(chamado);
    }

    public void rejeitarChamado(Integer chamadoId) {
        ChamadoEndereco chamado = chamadoEnderecoRepository.findByIdAndStatus(chamadoId, ChamadoEndereco.StatusChamado.ABERTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum chamado aberto encontrado para o com ID: " + chamadoId));

        chamado.setStatus(ChamadoEndereco.StatusChamado.REJEITADO);
        chamado.setUpdated_at(java.time.LocalDateTime.now());

        chamadoEnderecoRepository.save(chamado);
    }

    public List<ChamadoEnderecoDto> getChamadosPendentes() {

        return chamadoEnderecoRepository.findAllByStatus(ChamadoEndereco.StatusChamado.ABERTO)
                .stream()
                .map(ChamadoEnderecoMapper::toDto)
                .toList();

    }
}








