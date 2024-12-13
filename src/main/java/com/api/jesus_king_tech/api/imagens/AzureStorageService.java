package com.api.jesus_king_tech.api.imagens;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class AzureStorageService {


    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public ImgPerfil uploadFile(MultipartFile file) throws IOException {
        // Gerar um nome único para o arquivo
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
        System.out.println(credential);
        System.out.println(accountName);
        System.out.println(accountKey);
        System.out.println(containerName);

        // Criar o cliente Blob
        String endpoint = String.format("https://%s.blob.core.windows.net", accountName);

        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .containerName(containerName)
                .blobName(fileName)
                .buildClient();

        // Fazer o upload do arquivo
        blobClient.upload(file.getInputStream(), file.getSize(), true);


        // Retornar a URL do arquivo
        return ImgPerfil.builder()
                .url(blobClient.getBlobUrl())
                .blobName(blobClient.getBlobName())
                .build();
    }

    public void deleteFile(String fotoPerfil) {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        String endpoint = String.format("https://%s.blob.core.windows.net", accountName);

        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .containerName(containerName)
                .blobName(fotoPerfil)
                .buildClient();

        blobClient.delete();
    }
}
