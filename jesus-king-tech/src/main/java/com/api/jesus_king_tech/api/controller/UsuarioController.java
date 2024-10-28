package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioLoginDto;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioTokenDto;
import com.api.jesus_king_tech.domain.usuario.dto.*;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.service.UsuarioService;
import com.api.jesus_king_tech.swagger.controllers_openApi.UsuariosControllerOpenApi;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
    public class UsuarioController implements UsuariosControllerOpenApi {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private List<ValidacaoUsuarioStrategy> validacoes;


    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@Valid @RequestBody UsuarioCriarDto novoUsuario) {

        Usuario usuarioSalvar = UsuarioMapper.usuarioDtoToEntity(novoUsuario);

        Usuario usuarioSalvo = usuarioService.criarUsuario(usuarioSalvar);

        UsuarioResponseDto usuarioResponseDto = UsuarioMapper.usuarioEntityToDto(usuarioSalvo);

        return ResponseEntity.status(201).body(usuarioResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        List<Usuario> listagem = usuarioService.listarUsuarios();

        if (listagem.isEmpty()){
            ResponseEntity.status(204).build();
        }

//        Essa lista está sendo mapeada como um for
//        Pega cada objeto da lista listagem, e aplica o metodo UsuarioMapper.usuarioEntityToDto()
        List<UsuarioResponseDto> dtos =listagem
                .stream()
                .map(UsuarioMapper::usuarioEntityToDto)
                .toList();

        return ResponseEntity.status(200).body(dtos);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> EnviarCodigoRecuperarSenha(@RequestParam String emailUsuario) {

        usuarioService.enviarCodigoRecuperarSenha(emailUsuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Email enviado com sucesso");
    }

    @PostMapping("/recuperar-senha/validar-codigo")
    public ResponseEntity<String> validarCodigoRecuperacaoSenha(@RequestBody UsuarioValidarSenhaDto validarSenhaDto) {
        usuarioService.validarCodigoRecuperacaoSenha(validarSenhaDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Código Valido");
    }

    @PatchMapping("/recuperar-senha/nova-senha")
    public ResponseEntity<String> novaSenha(@RequestBody UsuarioMudarSenhaDto mudarSenhaDto) {
        usuarioService.mudarSenha(mudarSenhaDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Senha alterada com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuarioPorId(@PathVariable Integer id) {
        usuarioService.deletarUsuarioPorId(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioAtualizarDto usuario){

        Usuario usuarioAtualizar = UsuarioMapper.usuarioAtualizarToUsuarioEntity(usuario);

        Usuario usuarioAtualizado = usuarioService.atualizarUsuarioPorId(id, usuarioAtualizar);

        UsuarioResponseDto usuarioResponseDto = UsuarioMapper.usuarioEntityToDto(usuarioAtualizado);

        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        System.out.println("Chegou no controller, iniciando processo de auth");
        UsuarioTokenDto usuarioToken = usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioToken);
    }
}
