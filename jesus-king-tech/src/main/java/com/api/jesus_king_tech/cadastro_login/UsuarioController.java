package com.api.jesus_king_tech.cadastro_login;

import com.api.jesus_king_tech.cadastro_login.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.util.EmailUtil;
import com.api.jesus_king_tech.util.PasswordUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private List<ValidacaoUsuarioStrategy> validacoes;


    @PostMapping()
    public ResponseEntity<String> cadastrarUsuario(@Valid @RequestBody Usuario novoUsuario) {
        // esse for faz o novo usuário percorrer as validações que estão na ValidaçãoUsuarioStrategy
        for (int i = 0; i < validacoes.size(); i++) {
            if (!validacoes.get(i).validar(novoUsuario)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(validacoes.get(i).respostaErro());
            }
        }

        if (usuarioRepository.findByEmail(novoUsuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email já cadastrado");
        }

        String hashSenha = PasswordUtil.encoder(novoUsuario.getSenha());
        novoUsuario.setSenha(hashSenha);

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário cadastrado com sucesso !");
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestParam String emailUsuario) {
        System.out.println(emailUsuario);

        if (!usuarioRepository.existsByEmail(emailUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Email não cadastrado");
        }
        JavaMail.sendEmail(emailUsuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Email enviado com sucesso");
    }

    @PostMapping("/recuperar-senha/validar-codigo")
    public ResponseEntity<String> validarCodigoRecuperacaoSenha(@RequestBody String codigo) {
        if (codigo.equals(JavaMail.getCode())) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Código válido");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Código inválido");
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginAuth loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha inválidos");
        }

        boolean senhaValida = PasswordUtil.senhaCorreta(loginRequest.getSenha(), usuario.getSenha());
        if (!senhaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha inválidos");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Login realizado com sucesso");
    }


}
