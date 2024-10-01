package com.api.jesus_king_tech.api.controller;

import com.api.jesus_king_tech.domain.usuario.dto.UsuarioCriarDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMapper;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioValidarSenhaDto;
import com.api.jesus_king_tech.service.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import com.api.jesus_king_tech.service.UsuarioService;
import com.api.jesus_king_tech.util.PasswordUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final UsuarioService usuarioService;

    @Autowired
    private List<ValidacaoUsuarioStrategy> validacoes;


    @PostMapping()
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
    public ResponseEntity<String> novaSenha(@RequestBody Map<String, String> recuperarSenha) {
        System.out.println(recuperarSenha.get("email"));
        Usuario usuario = usuarioRepository.findByEmail(recuperarSenha.get("email"));
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Email não cadastrado");
        }
        String hashSenha = PasswordUtil.encoder(recuperarSenha.get("novaSenha"));
        usuario.setSenha(hashSenha);
        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Senha alterada com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuário deletado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuário não encontrado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario){
        if(usuarioRepository.existsById(id)){
            usuario.setId(id);
//            VALIDA AS INFORMAÇÕES DO USUÁRIO
            for (int i = 0; i < validacoes.size(); i++) {
                if (!validacoes.get(i).validar(usuario)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(validacoes.get(i).respostaErro());
                }
            }
            String hashSenha = PasswordUtil.encoder(usuario.getSenha());
            usuario.setSenha(hashSenha);

            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuário atualizado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Usuário não encontrado");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginAuth loginRequest) {
//        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());
//        if (usuario == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Email ou senha inválidos");
//        }
//
//        boolean senhaValida = PasswordUtil.senhaCorreta(loginRequest.getSenha(), usuario.getSenha());
//        if (!senhaValida) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Email ou senha inválidos");
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body("Login realizado com sucesso");
//    }


}
