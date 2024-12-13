package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.api.observer.AdminEmailObserver;
import com.api.jesus_king_tech.api.observer.UsuarioSubject;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioLoginDto;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioTokenDto;
import com.api.jesus_king_tech.domain.usuario.dto.*;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.service.UsuarioService;
import com.api.jesus_king_tech.swagger.controllers_openApi.UsuariosControllerOpenApi;
import com.api.jesus_king_tech.util.EmailUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import com.azure.core.annotation.Put;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
    public class UsuarioController implements UsuariosControllerOpenApi {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private List<ValidacaoUsuarioStrategy> validacoes;

    @Autowired
    private UsuarioSubject usuarioSubject;



    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(@Valid @RequestBody UsuarioCriarDto novoUsuario) {

        Usuario usuarioSalvar = UsuarioMapper.usuarioDtoToEntity(novoUsuario);

        Usuario usuarioSalvo = usuarioService.criarUsuario(usuarioSalvar);

        UsuarioResponseDto usuarioResponseDto = UsuarioMapper.usuarioEntityToDto(usuarioSalvo);

        return ResponseEntity.status(201).body(usuarioResponseDto);
    }

    @PostMapping("/cadastrar/foto-perfil/{id}")
    public ResponseEntity<UsuarioResponseDto> cadastrarFotoPerfil(@PathVariable Integer id, @RequestParam("file") MultipartFile fotoPerfil) {
        Usuario usuario = usuarioService.cadastrarFotoPerfil(id, fotoPerfil);

        UsuarioResponseDto usuarioResponseDto = UsuarioMapper.usuarioEntityToDto(usuario);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDto);
    }

    @DeleteMapping("/deletar-foto-perfil/{id}")
    public ResponseEntity<UsuarioResponseDto> deletarFotoPerfil(@PathVariable Integer id) {
       usuarioService.deletarFotoPerfil(id);


        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);

        UsuarioResponseDto usuarioResponseDto = UsuarioMapper.usuarioEntityToDto(usuario);

        return ResponseEntity.status(200).body(usuarioResponseDto);
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> EnviarCodigoRecuperarSenha(@RequestParam String emailUsuario) {

        usuarioService.enviarCodigoRecuperarSenha(emailUsuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Email enviado com sucesso");
    }

    @PostMapping("/recuperar-senha/validar-codigo")
    public ResponseEntity<String> validarCodigoRecuperacaoSenha(@RequestBody UsuarioValidarCodigoDto validarCodigoDto) {
        usuarioService.validarCodigoRecuperacaoSenha(validarCodigoDto);

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

    @PutMapping("/simples/{id}")
    public ResponseEntity<String> atualizarUsuarioSimples(@PathVariable Integer id, @RequestBody UsuarioAtualizarSimplesDto usuario) {


        String usuarioTokenDto = usuarioService.atualizarSimplesUsuarioPorId(id, usuario);


        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioTokenDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        System.out.println("Chegou no controller, iniciando processo de auth");
        UsuarioTokenDto usuarioToken = usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioToken);
    }

    @GetMapping("/ordenados-logradouro")
    public ResponseEntity<List<Map<String, Object>>> getUsuariosOrdenadosPorLogradouro() {
        List<Usuario> usuariosOrdenados = usuarioService.getUsuariosOrdenadosPorLogradouro();

        if (usuariosOrdenados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<Map<String, Object>> usuariosPublicos = usuariosOrdenados.stream()
                .map(usuario -> Map.of(
                        "id", usuario.getId(),
                        "nome", usuario.getNome(),
                        "genero", usuario.getGenero(),
                        "telefone", usuario.getTelefone(),
                        "endereco", Map.of(
                                "cep", usuario.getEndereco().getCep(),
                                "logradouro", usuario.getEndereco().getLogradouro(),
                                "numero", usuario.getEndereco().getNumero(),
                                "complemento", usuario.getEndereco().getComplemento(),
                                "bairro", usuario.getEndereco().getBairro(),
                                "localidade", usuario.getEndereco().getLocalidade(),
                                "uf", usuario.getEndereco().getUf()
                        )
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuariosPublicos);
    }


    @GetMapping("/buscar-logradouro/{logradouro}")
    public ResponseEntity<List<Map<String, Object>>> buscarUsuarioPorLogradouro(@PathVariable String logradouro) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorLogradouro(logradouro);

        return usuarioOpt.map(usuario -> {
            List<Map<String, Object>> usuariosPublicos = List.of(
                    Map.of(
                            "id", usuario.getId(),
                            "nome", usuario.getNome(),
                            "genero", usuario.getGenero(),
                            "telefone", usuario.getTelefone(),
                            "endereco", Map.of(
                                    "cep", usuario.getEndereco().getCep(),
                                    "logradouro", usuario.getEndereco().getLogradouro(),
                                    "numero", usuario.getEndereco().getNumero(),
                                    "complemento", usuario.getEndereco().getComplemento(),
                                    "bairro", usuario.getEndereco().getBairro(),
                                    "localidade", usuario.getEndereco().getLocalidade(),
                                    "uf", usuario.getEndereco().getUf()
                            )
                    )
            );
            return ResponseEntity.ok(usuariosPublicos);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/exportar")
    public ResponseEntity<String> exportarUsuarios() {
        try {
            String caminhoArquivo = usuarioService.exportarUsuariosParaCsv();
            return ResponseEntity.ok("Usuários exportados com sucesso: " + caminhoArquivo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao exportar usuários: " + e.getMessage());
        }
    }



}
