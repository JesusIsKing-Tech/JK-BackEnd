package com.api.jesus_king_tech.api.imagens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/imagens")
public class imagensController {

    @Autowired
    private  AzureStorageService azureStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println("Entrei no endpoint");
        try {
            String url = azureStorageService.uploadFile(file).getUrl();
            return ResponseEntity.status(201).body(url);
        }catch ( IOException e){
            return ResponseEntity.status(500).body("Erro ao fazer upload da imagem" + e.toString());
        }
    }

}
