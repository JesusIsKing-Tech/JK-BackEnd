package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.chamado_endereco.ChamadoEndereco;
import com.api.jesus_king_tech.domain.chamado_endereco.dto.ChamadoEnderecoDto;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoResponse;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoViaCepDTO;
import com.api.jesus_king_tech.service.EnderecoService;
import com.api.jesus_king_tech.swagger.controllers_openApi.EnderecoControllerOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController implements EnderecoControllerOpenApi {



    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<EnderecoResponse>> getAllEnderecos() {
        List<EnderecoResponse> enderecos = enderecoService.getEnderecosOrdenados();
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> getEnderecoById(@PathVariable Integer id) {
        return enderecoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<EnderecoResponse> criarEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO) {
        EnderecoResponse novoEndereco = enderecoService.save(enderecoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Integer id) {
        if (!enderecoService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        enderecoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/visualizar-lista")
    public ResponseEntity<List<EnderecoResponse>> visualizarEnderecosListaEstatica() {
        List<EnderecoResponse> enderecos = enderecoService.getListaEnderecos().exibir();
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/buscar/{cep}")
    public ResponseEntity<EnderecoViaCepDTO> buscarEnderecoPorCep(@PathVariable String cep) {
        EnderecoViaCepDTO endereco = enderecoService.buscarEnderecoPorCep(cep);
        if (endereco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/ordenados")
    public ResponseEntity<List<EnderecoResponse>> getEnderecosOrdenados() {
        List<EnderecoResponse> enderecosOrdenados = enderecoService.getEnderecosOrdenados();
        if (enderecosOrdenados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(enderecosOrdenados);
    }

    @PostMapping("/verificar")
    public ResponseEntity<Integer> verificarEndereco(@RequestBody EnderecoDTO enderecoDTO) throws ClassNotFoundException {
        Integer enderecoId = enderecoService.verificarEndereco(enderecoDTO);
        return ResponseEntity.ok(enderecoId);
    }

    @PostMapping("/{userId}/abrir-chamado")
    public ResponseEntity<ChamadoEnderecoDto> abrirChamado(@RequestBody EnderecoDTO enderecoDTO, @PathVariable Integer userId) {
       ChamadoEnderecoDto chamado = enderecoService.abrirChamado(enderecoDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chamado);
    }

    @PostMapping("/{chamadoId}/aprovar-chamado")
    public ResponseEntity<Void> aprovarChamado(@PathVariable Integer chamadoId) {
        enderecoService.aprovarChamado(chamadoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{chamadoId}/rejeitar-chamado")
    public ResponseEntity<Void> rejeitarChamado(@PathVariable Integer chamadoId) {
        enderecoService.rejeitarChamado(chamadoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/chamados")
    public ResponseEntity<List<ChamadoEnderecoDto>> getChamadosPendentes() {
        List<ChamadoEnderecoDto> chamadosPendentes = enderecoService.getChamadosPendentes();

        if (chamadosPendentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(chamadosPendentes);
    }

}

