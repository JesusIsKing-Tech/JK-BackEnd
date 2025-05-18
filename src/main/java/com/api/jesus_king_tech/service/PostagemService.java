package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.postagem.Postagem;
import com.api.jesus_king_tech.domain.postagem.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class  PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    public List<Postagem> listar() {
        return postagemRepository.findAll();
    }

    public Postagem salvar(Postagem postagem, MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            postagem.setImagem(file.getBytes());
            postagem.setImagemMimeType(file.getContentType());
        }
        return postagemRepository.save(postagem);


    }

    public List<Postagem> postagemSemana() {

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        return postagemRepository.findByDataBetween(today, nextWeek);
    }

    public Optional<Postagem> buscarPorId(Integer id) {
        return postagemRepository.findById(id);
    }
}
