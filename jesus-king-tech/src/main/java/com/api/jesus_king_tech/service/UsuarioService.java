package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.api.security.jwt.GerenciadorTokenJwt;
import com.api.jesus_king_tech.domain.endereco.dto.EnderecoResponse;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioLoginDto;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioTokenDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMapper;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMudarSenhaDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioResponseDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioValidarSenhaDto;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import com.api.jesus_king_tech.exception.ExceptionHttp;
import com.api.jesus_king_tech.service.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.util.PasswordUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

//    Injetando dependencias da classe service
    private final UsuarioRepository usuarioRepository;
    private final List<ValidacaoUsuarioStrategy> validacoes;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoService enderecoService;


    public Usuario criarUsuario(Usuario novoUsuario) {
        novoUsuario.setId(null);
        for (int i = 0; i < validacoes.size(); i++) {
            if (!validacoes.get(i).validar(novoUsuario)) {
                throw new ExceptionHttp(validacoes.get(i).respostaErro(), HttpStatus.BAD_REQUEST);
            }
        }

        if (usuarioRepository.findByEmail(novoUsuario.getEmail()).isPresent()) {
            throw new ExceptionHttp("Este email já está cadastrado", HttpStatus.CONFLICT);
        }

        String hashSenha = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(hashSenha);

        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> listarUsuarios() {
      return usuarioRepository.findAll();
    }

    public void enviarCodigoRecuperarSenha(String emailUsuario){

        Optional<Usuario> usuarioEmail = usuarioRepository.findByEmail(emailUsuario);

        if (usuarioEmail.isEmpty()){
            throw new ExceptionHttp("Email não cadastrado", HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioEmail.get();

        JavaMail.sendEmail(emailUsuario, usuario.getNome());
        System.out.println("Esse é o codigo pro email" + JavaMail.getCode());
        usuario.setCodigo_recuperar_senha(JavaMail.getCode());

        LocalDateTime validade = LocalDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.SECONDS);
        usuario.setValidade_codigo_senha(validade);
        System.out.println("Essa é a validade" + validade);

        usuarioRepository.save(usuario);

    }

    public void validarCodigoRecuperacaoSenha(UsuarioValidarSenhaDto validarSenhaDto) {
        Optional<Usuario> usuarioEmail = usuarioRepository.findByEmail(validarSenhaDto.getEmail());

        if (usuarioEmail.isEmpty()){
            throw new ExceptionHttp("Email não cadastrado", HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioEmail.get();

        if (usuario.getValidade_codigo_senha().isBefore(LocalDateTime.now())){
            throw new ExceptionHttp("A validade do codigo expirou", HttpStatus.BAD_REQUEST);
        }

        if (!validarSenhaDto.getCodigo_recuperar_senha().equals(usuario.getCodigo_recuperar_senha())){
            throw new ExceptionHttp("Codigo de recuperação está invalido !", HttpStatus.BAD_REQUEST);
        }
    }

    public void mudarSenha(UsuarioMudarSenhaDto mudarSenhaDto) {
        Optional<Usuario> usuarioEmail = usuarioRepository.findByEmail(mudarSenhaDto.getEmail());

        if (usuarioEmail.isEmpty()){
            throw new ExceptionHttp("Email não cadastrado", HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioEmail.get();

        if (usuario.getValidade_codigo_senha() == null){
            throw new ExceptionHttp("Nenhum codigo de validação encontrado, solicite um novo", HttpStatus.NOT_FOUND);
        }

        if (usuario.getValidade_codigo_senha().isBefore(LocalDateTime.now())){
            throw new ExceptionHttp("A válidade do código expirou, solicite um novo", HttpStatus.BAD_REQUEST);
        }

        if (!PasswordUtil.validarSenha(mudarSenhaDto.getSenha())){
            throw new ExceptionHttp(PasswordUtil.respostaErroSenha(), HttpStatus.BAD_REQUEST);
        }

        String hashSenha = passwordEncoder.encode(mudarSenhaDto.getSenha());

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

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto){

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

        final Authentication authentication = authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado =
                usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);

    }




    public String exportarUsuariosParaCsv() throws IOException {
        List<Usuario> usuarios = listarUsuarios();
        String caminhoArquivo = "usuarios.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write("ID,Nome,Email,Telefone,Data de Nascimento,Gênero");
            writer.newLine();

            for (Usuario usuario : usuarios) {
                String linha = String.join(",",
                        usuario.getId().toString(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getData_nascimento().toString(),
                        usuario.getGenero()
                );
                writer.write(linha);
                writer.newLine();
            }
        }

        return caminhoArquivo;
    }





}


