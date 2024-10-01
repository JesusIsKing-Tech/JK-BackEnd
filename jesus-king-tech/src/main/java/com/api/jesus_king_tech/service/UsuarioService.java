package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMudarSenhaDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioValidarSenhaDto;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import com.api.jesus_king_tech.exception.ExceptionHttp;
import com.api.jesus_king_tech.service.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.util.PasswordUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

//    Injetando dependencias da classe service
    private final UsuarioRepository usuarioRepository;
    private final List<ValidacaoUsuarioStrategy> validacoes;

    public Usuario criarUsuario(Usuario novoUsuario) {
        novoUsuario.setId(null);
        for (int i = 0; i < validacoes.size(); i++) {
            if (!validacoes.get(i).validar(novoUsuario)) {
                throw new ExceptionHttp(validacoes.get(i).respostaErro(), HttpStatus.BAD_REQUEST);
            }
        }

        if (usuarioRepository.findByEmail(novoUsuario.getEmail()) != null) {
            throw new ExceptionHttp("Este email já está cadastrado", HttpStatus.CONFLICT);
        }

        String hashSenha = PasswordUtil.encoder(novoUsuario.getSenha());
        novoUsuario.setSenha(hashSenha);

        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> listarUsuarios() {
      return usuarioRepository.findAll();
    }

    public void enviarCodigoRecuperarSenha(String emailUsuario){
        if(!usuarioRepository.existsByEmail(emailUsuario)){
            throw new ExceptionHttp("Email não cadastrado", HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario);

        JavaMail.sendEmail(emailUsuario, usuario.getNome());
        System.out.println("Esse é o codigo pro email" + JavaMail.getCode());
        usuario.setCodigo_recuperar_senha(JavaMail.getCode());

        LocalDateTime validade = LocalDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.SECONDS);
        usuario.setValidade_codigo_senha(validade);
        System.out.println("Essa é a validade" + validade);

        usuarioRepository.save(usuario);

    }

    public void validarCodigoRecuperacaoSenha(UsuarioValidarSenhaDto validarSenhaDto) {
        Usuario usuario = usuarioRepository.findByEmail(validarSenhaDto.getEmail());

        if (usuario == null){
            throw new ExceptionHttp("Email não cadastrado", HttpStatus.NOT_FOUND);
        }

        if (usuario.getValidade_codigo_senha().isBefore(LocalDateTime.now())){
            throw new ExceptionHttp("A validade do codigo expirou", HttpStatus.BAD_REQUEST);
        }

        if (!validarSenhaDto.getCodigo_recuperar_senha().equals(usuario.getCodigo_recuperar_senha())){
            throw new ExceptionHttp("Codigo de recuperação está invalido !", HttpStatus.BAD_REQUEST);
        }
    }

    public void mudarSenha(UsuarioMudarSenhaDto mudarSenhaDto) {
        Usuario usuario = usuarioRepository.findByEmail(mudarSenhaDto.getEmail());

        if (usuario == null){
            throw new ExceptionHttp("Email não cadastrado", HttpStatus.NOT_FOUND);
        }

        if (usuario.getValidade_codigo_senha() == null){
            throw new ExceptionHttp("Nenhum codigo de validação encontrado, solicite um novo", HttpStatus.NOT_FOUND);
        }

        if (usuario.getValidade_codigo_senha().isBefore(LocalDateTime.now())){
            throw new ExceptionHttp("A válidade do código expirou, solicite um novo", HttpStatus.BAD_REQUEST);
        }

        if (!PasswordUtil.validarSenha(mudarSenhaDto.getSenha())){
            throw new ExceptionHttp(PasswordUtil.respostaErroSenha(), HttpStatus.BAD_REQUEST);
        }

        String hashSenha = PasswordUtil.encoder(mudarSenhaDto.getSenha());

        usuario.setSenha(hashSenha);
        usuario.setCodigo_recuperar_senha(null);
        usuario.setValidade_codigo_senha(null);

        usuarioRepository.save(usuario);
    }

    public void deletarUsuarioPorId(Integer id){

        if (!usuarioRepository.existsById(id)){
            throw new ExceptionHttp("Não foi encontrado nenhum usuario com esse id", HttpStatus.NOT_FOUND);
        }

        usuarioRepository.deleteById(id);

    }

    public Usuario atualizarUsuarioPorId(Integer id, Usuario usuarioAtualizar){

        if (!usuarioRepository.existsById(id)){
            throw new ExceptionHttp("Nenhum usuario com o Id: " + id + " Encontrado", HttpStatus.NOT_FOUND);
        }

            usuarioAtualizar.setId(id);
//            VALIDA AS INFORMAÇÕES DO USUÁRIO
            for (int i = 0; i < validacoes.size(); i++) {
                if (!validacoes.get(i).validar(usuarioAtualizar)) {
                    throw new ExceptionHttp(validacoes.get(i).respostaErro(), HttpStatus.BAD_REQUEST);
                }
            }
            String hashSenha = PasswordUtil.encoder(usuarioAtualizar.getSenha());
            usuarioAtualizar.setSenha(hashSenha);

            return usuarioRepository.save(usuarioAtualizar);

    }
}
