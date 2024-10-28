package com.api.jesus_king_tech.swagger.controllers_openApi;

import com.api.jesus_king_tech.domain.endereco.dto.EnderecoDTO;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoResponse;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoViaCepDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Endereços", description = "Gerencia os endereços")
public interface EnderecoControllerOpenApi {

    ResponseEntity<List<EnderecoResponse>> getAllEnderecos();

    ResponseEntity<EnderecoResponse> getEnderecoById(@PathVariable Integer id);

    ResponseEntity<EnderecoResponse> criarEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO);

    ResponseEntity<Void> deletarEndereco(@PathVariable Integer id);

    ResponseEntity<EnderecoViaCepDTO> buscarEnderecoPorCep(@PathVariable String cep);

    ResponseEntity<List<EnderecoResponse>> getEnderecosOrdenados();
}
