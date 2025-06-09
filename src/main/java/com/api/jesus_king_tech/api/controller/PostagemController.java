package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoMapper;
import com.api.jesus_king_tech.domain.endereco.repository.EnderecoRepository;
import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.domain.postagem.dto.PostagemDto;
import com.api.jesus_king_tech.domain.postagem.dto.PostagemMapper;
import com.api.jesus_king_tech.service.EnderecoService;
import com.api.jesus_king_tech.service.PostagemService;
import org.bouncycastle.asn1.x509.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/postagem")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<PostagemDto>> listarPostagens() {
        List<Postagem> listagem = postagemService.listar();

        if (listagem.isEmpty()){
            ResponseEntity.status(204).build();
        }

        // Retorna a lista de postagens
        List<PostagemDto> listagemDto = PostagemMapper.toDtoList(listagem);

        return ResponseEntity.ok(listagemDto);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Postagem> cadastrarPostagem(@RequestParam(value = "file", required = false) MultipartFile file,
                                                      @RequestParam("titulo") String tituloPostagem,
                                                      @RequestParam("data") String dataPostagem,
                                                      @RequestParam("hora") String horaEvento,
                                                      @RequestParam(value = "descricao", required = false) String descricao,
                                                      @RequestParam(value = "valor", required = false) Double valor,
                                                      @RequestPart("enderecoEvento") EnderecoDTO enderecoEvento) throws IOException, ParseException {

        // Conversão manual da data
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataConvertida = new java.sql.Date(dateFormat.parse(dataPostagem).getTime());
        java.time.LocalTime horaConvertida = java.time.LocalTime.parse(horaEvento);

        // Verifica se o endereço existe no banco de dados
        Endereco endereco = enderecoService.validarOuCriarEndereco(enderecoEvento);

        // Construção da nova postagem
        Postagem novaPostagem = Postagem.builder()
                .titulo(tituloPostagem)
                .data(dataConvertida.toLocalDate())
                .horaEvento(horaConvertida) // Hora do evento
                .descricao(descricao)
                .valor(valor)// Descrição opcional
                .endereco(endereco) // Endereço associado
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
    public ResponseEntity<byte[]> obterFotoEvento(@PathVariable Integer id) {

        byte[] foto = postagemService.obterFotoEvento(id);
        if (foto == null) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).header("Content-Type", "image/jpeg")
                .body(foto);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Postagem> atualizarPostagem(@PathVariable Integer id,
                                                      @RequestParam(value = "file", required = false) MultipartFile file,
                                                      @RequestParam("titulo") String tituloPostagem,
                                                      @RequestParam("data") String dataPostagem,
                                                      @RequestParam("hora") String horaEvento,
                                                      @RequestParam(value = "descricao", required = false) String descricao,
                                                      @RequestParam(value = "valor", required = false) Double valor,
                                                      @RequestPart("enderecoEvento") EnderecoDTO enderecoEvento) throws IOException, ParseException {

        // Busca a postagem pelo ID
        Postagem postagemExistente = postagemService.buscarPorId(id);
        if (postagemExistente == null) {
            return ResponseEntity.status(404).build(); // Retorna NOT FOUND se a postagem não existir
        }

        // Conversão manual da data
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataConvertida = new java.sql.Date(dateFormat.parse(dataPostagem).getTime());
        java.time.LocalTime horaConvertida = java.time.LocalTime.parse(horaEvento);

        // Atualiza os campos fornecidos
        postagemExistente.setTitulo(tituloPostagem);
        postagemExistente.setData(dataConvertida.toLocalDate());
        postagemExistente.setHoraEvento(horaConvertida);
        postagemExistente.setDescricao(descricao);
        postagemExistente.setValor(valor);

        // Verifica e atualiza o endereço
        Endereco endereco = enderecoService.validarOuCriarEndereco(enderecoEvento);
        postagemExistente.setEndereco(endereco);

        // Salva a postagem atualizada
        Postagem postagemAtualizada = postagemService.salvar(postagemExistente, file);

        return ResponseEntity.ok(postagemAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPostagem(@PathVariable Integer id) {
        Postagem postagem = postagemService.buscarPorId(id);
        if (postagem == null) {
            return ResponseEntity.status(404).build(); // Retorna NOT FOUND se a postagem não existir
        }
        postagemService.deletar(id);
        return ResponseEntity.status(204).build(); // Retorna NO CONTENT após a exclusão
    }

}
