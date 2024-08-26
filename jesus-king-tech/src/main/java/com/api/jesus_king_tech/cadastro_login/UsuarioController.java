package com.api.jesus_king_tech.cadastro_login;

import com.api.jesus_king_tech.util.EmailUtil;
import com.api.jesus_king_tech.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
