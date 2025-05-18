package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.service.PostagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Postagem> cadastrarPostagem(@RequestParam(value = "file", required = false) MultipartFile file,
                                                      @RequestParam("titulo") String tituloPostagem,
                                                      @RequestParam("data") String dataPostagem,
                                                      @RequestParam(value = "descricao", required = false) String descricao,
                                                      @RequestParam(value = "valor", required = false) Double valor) throws IOException, ParseException {

        // Conversão manual da data
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataConvertida = new java.sql.Date(dateFormat.parse(dataPostagem).getTime());


        // Construção da nova postagem
        Postagem novaPostagem = Postagem.builder()
                .titulo(tituloPostagem)
                .data(dataConvertida)
                .descricao(descricao)
                .valor(valor)// Descrição opcional
                .build();

        // Salvar a postagem com ou sem imagem
        Postagem postagem = postagemService.salvar(novaPostagem, file);

        return ResponseEntity.status(201).body(postagem);
    }

    @GetMapping("/semanal")
    public ResponseEntity<List<Postagem>> postagemSemana(){
        List<Postagem> postagemSemana = postagemService.postagemSemana();
        return ResponseEntity.ok(postagemSemana);
    }

    @GetMapping("/foto-evento/{id}")
    public ResponseEntity<byte[]> getImagem(@PathVariable Integer id) {
        System.out.println("ID recebido: " + id);
        Postagem postagem = postagemService.buscarPorId(id).get();

        if (postagem.getImagem() != null) {
            return ResponseEntity.status(200).header("Content-Type", postagem.getImagemMimeType())
                    .body(postagem.getImagem());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
