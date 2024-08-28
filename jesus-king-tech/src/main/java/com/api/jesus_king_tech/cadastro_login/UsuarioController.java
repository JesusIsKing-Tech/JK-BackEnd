package com.api.jesus_king_tech.cadastro_login;

import com.api.jesus_king_tech.cadastro_login.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.util.EmailUtil;
import com.api.jesus_king_tech.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario novoUsuario){
        if (!PasswordUtil.senhaValida(novoUsuario.getSenha())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("""
                            Senha invalida, a senha deve conter:
                            1 letra maiúscula,
                            1 letra minúscula,
                            1 número,
                            1 caractere especial,
                            no minimo 6 caracteres.
                            """
                    );
        }
        String hashSenha = PasswordUtil.encoder(novoUsuario.getSenha());
        novoUsuario.setSenha(hashSenha);

        if (!EmailUtil.emailValido(novoUsuario.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email inválido" +
                            "Digite um email válido : joao@exemplo.com"
                    );
        }

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário cadastrado com sucesso");
    }

    @GetMapping
    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody String emailUsuario){
        System.out.println(emailUsuario);

        if (!usuarioRepository.existsByEmail(emailUsuario)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Email não cadastrado");
        }
        JavaMail.sendEmail(emailUsuario);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Email enviado com sucesso");
    }

    @PostMapping("/recuperar-senha/validar-codigo")
    public ResponseEntity<String> validarCodigoRecuperacaoSenha(@RequestBody String codigo){
        if (codigo.equals(JavaMail.getCode())){
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Código válido");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Código inválido");
    }




}
