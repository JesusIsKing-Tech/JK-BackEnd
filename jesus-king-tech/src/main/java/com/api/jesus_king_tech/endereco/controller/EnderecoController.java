package com.api.jesus_king_tech.endereco.controller;

import com.api.jesus_king_tech.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.endereco.entity.Endereco;
import com.api.jesus_king_tech.endereco.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<Endereco>> getAllEnderecos() {
        List<Endereco> enderecos = enderecoService.findAll();
        if (enderecos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(enderecos, HttpStatus.OK);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Endereco> getEnderecoById(@PathVariable Integer id) {
        Optional<Endereco> endereco = enderecoService.findById(id);
        return endereco.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Endereco> criarEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO) {
        Endereco novoEndereco = enderecoService.save(enderecoDTO);
        return ResponseEntity.status(201).body(novoEndereco);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Integer id) {
        if (!enderecoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        enderecoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

