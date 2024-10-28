package com.api.jesus_king_tech.swagger.controllers_openApi;

import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioLoginDto;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioTokenDto;
import com.api.jesus_king_tech.domain.usuario.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Usuários", description = "Gerencia os usuários")
public interface UsuariosControllerOpenApi {

    @Operation(summary = "Cadastrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@Valid @RequestBody UsuarioCriarDto novoUsuario);

    @Operation(summary = "Listar todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    })
    ResponseEntity<List<UsuarioResponseDto>> listarUsuarios();

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso, codigo enviado para o email"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    ResponseEntity<String> EnviarCodigoRecuperarSenha(@RequestParam String emailUsuario);

    @Operation(summary = "Validar código de recuperação de senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código validado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    ResponseEntity<String> validarCodigoRecuperacaoSenha(@RequestBody UsuarioValidarSenhaDto validarSenhaDto);

    @Operation(summary = "Alterar senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    ResponseEntity<String> novaSenha(@RequestBody UsuarioMudarSenhaDto mudarSenhaDto);

    @Operation(summary = "Deletar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    ResponseEntity<String> deletarUsuarioPorId(@PathVariable Integer id);

    @Operation(summary = "Atualizar usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioAtualizarDto usuario);

    @Operation(summary = "Login de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto);
}
