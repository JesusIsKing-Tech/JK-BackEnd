package com.api.jesus_king_tech.alimento;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alimentos")

public class AlimentoController {

    @Autowired
    AlimentoRepository alimentoRepository;



    @GetMapping
    public ResponseEntity<List<Alimento>> listarAlimentos() {
        List<Alimento> alimentos = alimentoRepository.findAll();
        if (alimentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(200).body(alimentos);
    }



    @PostMapping
    public ResponseEntity<Alimento> cadastrarAlimento(@RequestBody Alimento alimento) {

        Optional<Alimento> existenteOpt = alimentoRepository.findByNome(alimento.getNome());

        if (existenteOpt.isPresent()) {
            Alimento existente = existenteOpt.get();
            existente.setQuantidade(existente.getQuantidade() + 1);
            alimentoRepository.save(existente);
            return ResponseEntity.status(201).body(existente);
        } else {
            alimento.setQuantidade(1);
            alimentoRepository.save(alimento);
            return ResponseEntity.status(201).body(alimento);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Alimento> atualizarAlimento(@PathVariable Integer id, @RequestBody Alimento alimentoAtualizado) {
        Optional<Alimento> alimentoExistente = alimentoRepository.findById(id);

        if (alimentoExistente.isPresent()) {
            Alimento alimento = alimentoExistente.get();
            alimento.setNome(alimentoAtualizado.getNome());
            alimento.setQuantidade(alimentoAtualizado.getQuantidade());
            alimento.setPeso(alimentoAtualizado.getPeso());

            alimentoRepository.save(alimento);
            return ResponseEntity.ok(alimento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarAlimento(@PathVariable Integer id) {
        Optional<Alimento> alimentoOpt = alimentoRepository.findById(id);

        if (alimentoOpt.isPresent()) {
            Alimento alimento = alimentoOpt.get();

            if (alimento.getQuantidade() > 1) {
                alimento.setQuantidade(alimento.getQuantidade() - 1);
                alimentoRepository.save(alimento);
            } else {
                alimentoRepository.delete(alimento);
            }

            return ResponseEntity.status(200).body("Alimento removido ou quantidade diminuída!");
        }

        return ResponseEntity.status(404).body("Alimento não encontrado");
    }
}





