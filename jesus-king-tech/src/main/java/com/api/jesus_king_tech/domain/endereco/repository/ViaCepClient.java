package com.api.jesus_king_tech.domain.endereco.repository;

import com.api.jesus_king_tech.domain.endereco.dto.EnderecoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep-client", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    EnderecoDTO buscarEnderecoPorCep(@PathVariable("cep") String cep);
}
