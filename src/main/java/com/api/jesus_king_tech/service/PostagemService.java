//package com.api.jesus_king_tech.service;
//
//import com.api.jesus_king_tech.api.imagens.AzureStorageService;
//import com.api.jesus_king_tech.api.imagens.ImgPerfil;
//import com.api.jesus_king_tech.domain.postagem.Postagem;
//import com.api.jesus_king_tech.domain.postagem.repository.PostagemRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class PostagemService {
//
//    @Autowired
//    private PostagemRepository postagemRepository;
//
//    @Autowired
//    private AzureStorageService azureStorageService;
//
//    public List<Postagem> listar() {
//        return postagemRepository.findAll();
//    }
//
//    public Postagem salvar(Postagem postagem, MultipartFile file) throws IOException {
//
//        ImgPerfil blob = azureStorageService.uploadFile(file);
//
//        postagem.setUrl(blob.getUrl());
//        postagem.setBlobName(blob.getBlobName());
//
//        return postagemRepository.save(postagem);
//    }
//
//    public List<Postagem> postagemSemana() {
//
//        LocalDate today = LocalDate.now();
//        LocalDate nextWeek = today.plusDays(7);
//
//        return postagemRepository.findByDataBetween(today, nextWeek);
//    }
//}
