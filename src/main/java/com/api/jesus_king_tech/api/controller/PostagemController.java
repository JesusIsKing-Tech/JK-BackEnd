package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.service.PostagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/postagem")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @GetMapping
    public ResponseEntity<List<Postagem>> listarPostagens() {
        List<Postagem> listagem = postagemService.listar();

        if (listagem.isEmpty()){
            ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok(listagem);
    }

    @PostMapping
    public ResponseEntity<Postagem> cadastrarPostagem(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("titulo") String tituloPostagem,
                                                      @RequestParam("data") Date dataPostagem) throws IOException {


        Postagem novaPostagem = Postagem.builder()
                .titulo(tituloPostagem)
                .data(dataPostagem)
                .build();

            Postagem postagem = postagemService.salvar(novaPostagem, file);
            return ResponseEntity.status(201).body(postagem);


    }

    @GetMapping("/semanal")
    public ResponseEntity<List<Postagem>> postagemSemana(){
        List<Postagem> postagemSemana = postagemService.postagemSemana();
        return ResponseEntity.ok(postagemSemana);
    }

}
