package com.api.jesus_king_tech.service;

import com.api.jesus_king_tech.api.imagens.AzureStorageService;
import com.api.jesus_king_tech.api.imagens.ImgPerfil;
import com.api.jesus_king_tech.api.observer.AdminEmailObserver;
import com.api.jesus_king_tech.api.observer.UsuarioSubject;
import com.api.jesus_king_tech.api.security.jwt.GerenciadorTokenJwt;
import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.endereco.repository.EnderecoRepository;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioLoginDto;
import com.api.jesus_king_tech.domain.usuario.autenticacao.dto.UsuarioTokenDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioAtualizarSimplesDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMapper;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMudarSenhaDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioValidarCodigoDto;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import com.api.jesus_king_tech.exception.ExceptionHttp;
import com.api.jesus_king_tech.service.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.util.EmailUtil;
import com.api.jesus_king_tech.util.PasswordUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UsuarioService {

//    Injetando dependencias da classe service
    private final UsuarioRepository usuarioRepository;
    private final List<ValidacaoUsuarioStrategy> validacoes;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoService enderecoService;
    private final EnderecoRepository enderecoRepository;
    private final UsuarioSubject usuarioSubject;
    private final EmailUtil emailUtil;

    private final AzureStorageService azureStorageService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            List<ValidacaoUsuarioStrategy> validacoes,
            GerenciadorTokenJwt gerenciadorTokenJwt,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EnderecoService enderecoService,
            EnderecoRepository enderecoRepository,
            UsuarioSubject usuarioSubject,
            AdminEmailObserver adminEmailObserver,
            EmailUtil emailUtil,
            AzureStorageService azureStorageService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.validacoes = validacoes;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.enderecoService = enderecoService;
        this.enderecoRepository = enderecoRepository;
        this.usuarioSubject = usuarioSubject;
        this.emailUtil = emailUtil;
        this.usuarioSubject.addObserver(adminEmailObserver);
        this.azureStorageService = azureStorageService;
    }


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

        Endereco novoEndereco = novoUsuario.getEndereco();
        Endereco endereco = enderecoService.enderecoExiste(novoEndereco);

        if (endereco == null) {
         novoUsuario.setEndereco(enderecoRepository.save(novoEndereco));
        }else {
            novoUsuario.setEndereco(endereco);
        }

        usuarioSubject.notifyObservers("usuario_criado", novoUsuario);


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

    public void validarCodigoRecuperacaoSenha(UsuarioValidarCodigoDto validarSenhaDto) {
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

    public String atualizarSimplesUsuarioPorId(Integer id, UsuarioAtualizarSimplesDto usuarioAtualizar){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isEmpty()){
            throw new ExceptionHttp("Nenhum usuario com o Id: " + id + " Encontrado", HttpStatus.NOT_FOUND);
        }

        Usuario userFinal = usuario.get();

        Optional<Usuario> usuarioDoEmailOpt = usuarioRepository.findByEmail(usuarioAtualizar.getEmail());


        if (usuarioDoEmailOpt.isPresent()) {
            Usuario usuarioDoEmail = usuarioDoEmailOpt.get();
            if (!usuarioDoEmail.getId().equals(id)) {
                throw new ExceptionHttp("Este email já está cadastrado", HttpStatus.CONFLICT);
            }
        }

        userFinal.setNome(usuarioAtualizar.getNome());
        userFinal.setEmail(usuarioAtualizar.getEmail());
        userFinal.setTelefone(usuarioAtualizar.getTelefone());
        userFinal.setReceber_doacoes(usuarioAtualizar.isReceber_doacoes());

        if (!emailUtil.validar(userFinal)) {
            throw new ExceptionHttp(emailUtil.respostaErro(), HttpStatus.BAD_REQUEST);
        }

        Usuario usuarioAtualizado = usuarioRepository.save(userFinal);
        System.out.println("Usuario atualizado: " + usuarioAtualizado.getSenha() + " " + usuarioAtualizado.getEmail());

        String senhaSemHash = "{bcrypt}" + usuarioAtualizado.getSenha();

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Autenticar o usuário e gerar o token JWT

        final String token = gerenciadorTokenJwt.gerarTokenParaUsuarioExistente(usuarioAtualizado);

        return token;

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


    public List<Usuario> getUsuariosOrdenadosPorLogradouro() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        int n = usuarios.size();
        for (int i = 0; i < n - 1; i++) {
            int menorIndice = i;

            for (int j = i + 1; j < n; j++) {
                String logradouroAtual = usuarios.get(j).getEndereco().getLogradouro();
                String logradouroMenor = usuarios.get(menorIndice).getEndereco().getLogradouro();

                if (logradouroAtual.compareTo(logradouroMenor) < 0) {
                    menorIndice = j;
                }
            }

            Usuario aux = usuarios.get(menorIndice);
            usuarios.set(menorIndice, usuarios.get(i));
            usuarios.set(i, aux);
        }

        return usuarios;
    }


    public Optional<Usuario> buscarUsuarioPorLogradouro(String logradouro) {
        List<Usuario> usuariosOrdenados = getUsuariosOrdenadosPorLogradouro();

        int baixo = 0;
        int alto = usuariosOrdenados.size() - 1;

        while (baixo <= alto) {
            int meio = (baixo + alto) / 2;
            String logradouroMeio = usuariosOrdenados.get(meio).getEndereco().getLogradouro();

            if (logradouroMeio.equals(logradouro)) {
                return Optional.of(usuariosOrdenados.get(meio));
            } else if (logradouroMeio.compareTo(logradouro) < 0) {
                baixo = meio + 1;
            } else {
                alto = meio - 1;
            }
        }

        return Optional.empty();
    }




    public String exportarUsuariosParaCsv() throws IOException {
        List<Usuario> usuarios = listarUsuarios();
        String caminhoArquivo = "usuarios.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write("ID,Nome,Email,Telefone,Data de Nascimento,Gênero,Logradouro,Número,Complemento,Bairro,Cidade,CEP");
            writer.newLine();
            exportarUsuarioRecursivo(writer, usuarios, 0);
        }

        return caminhoArquivo;
    }

    private void exportarUsuarioRecursivo(BufferedWriter writer, List<Usuario> usuarios, int index) throws IOException {
        if (index >= usuarios.size()) {
            return;
        }

        Usuario usuario = usuarios.get(index);
        Endereco endereco = usuario.getEndereco();
        String linha = String.join(",",
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getData_nascimento().toString(),
                usuario.getGenero(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getLocalidade(),
                endereco.getCep()
        );
        writer.write(linha);
        writer.newLine();

        exportarUsuarioRecursivo(writer, usuarios, index + 1);
    }


    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @Transactional
    public Usuario cadastrarFotoPerfil(Integer id, MultipartFile fotoPerfil) {
        Usuario usuario = buscarUsuarioPorId(id);

        try {
            usuario.setFotoPerfil(fotoPerfil.getBytes());
            String contentType = fotoPerfil.getContentType();
            System.out.println("Content Type recebido: " + contentType);
            usuario.setFotoPerfilMimeType(contentType); // Use o tipo MIME real do arquivo
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar a foto de perfil");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletarFotoPerfil(Integer id) {
        Usuario usuario = buscarUsuarioPorId(id);

        if (usuario.getFotoPerfil() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto de perfil não encontrada");
        }

        usuario.setFotoPerfil(null);
        usuario.setFotoPerfilMimeType(null);

        usuarioRepository.save(usuario);
    }



    public List<Usuario> usuariosPorEndereco(int idEndereco) {
        Endereco endereco = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

        return usuarioRepository.findAllByEndereco(endereco);
    }


    }
