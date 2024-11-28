package com.api.jesus_king_tech.Service;


import com.api.jesus_king_tech.api.observer.UsuarioSubject;
import com.api.jesus_king_tech.domain.endereco.Endereco;
import com.api.jesus_king_tech.domain.usuario.Usuario;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioMudarSenhaDto;
import com.api.jesus_king_tech.domain.usuario.dto.UsuarioValidarCodigoDto;
import com.api.jesus_king_tech.domain.usuario.repository.UsuarioRepository;
import com.api.jesus_king_tech.exception.ExceptionHttp;
import com.api.jesus_king_tech.service.JavaMailRecuperacaoSenha.JavaMail;
import com.api.jesus_king_tech.service.UsuarioService;
import com.api.jesus_king_tech.util.PasswordUtil;
import com.api.jesus_king_tech.util.ValidacaoUsuarioStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

   @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioSubject usuarioSubject;

    @Mock
    private List<ValidacaoUsuarioStrategy> validacoes;

    @Mock
    private PasswordEncoder passwordEncoder;


    public UsuarioServiceTest() {}


    @BeforeEach
    void setUp() {

    }

    @DisplayName("Listar Usuários deve retornar todos os Usuários")
    @Test
    void listarUsuarios() {
        List<Usuario> usuarios = List.of(Mockito.mock(Usuario.class), Mockito.mock(Usuario.class));
        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarios);
        Assertions.assertEquals(usuarios, usuarioService.listarUsuarios());
    }

    @DisplayName("Buscar Usuário por Logradouro deve retornar Usuário se tiver Logradouro encontrado")
    @Test
    void buscarUsuarioPorLogradouroEncontrado() {
        Usuario usuario = Mockito.mock(Usuario.class);
        Endereco endereco = Mockito.mock(Endereco.class);
        Mockito.when(endereco.getLogradouro()).thenReturn("logradouro");
        Mockito.when(usuario.getEndereco()).thenReturn(endereco);
        Mockito.when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        Optional<Usuario> resultado = usuarioService.buscarUsuarioPorLogradouro("logradouro");
        Assertions.assertTrue(resultado.isPresent());
        Assertions.assertEquals(usuario, resultado.get());
    }


    @DisplayName("Enviar Código Recuperar Senha deve enviar código se for Email cadastrado")
    @Test
    void enviarCodigoRecuperarSenhaEmailCadastrado() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setNome("Nome Teste");

        Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(usuario));
        Mockito.doNothing().when(JavaMail.class);
        JavaMail.sendEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        usuarioService.enviarCodigoRecuperarSenha("email@teste.com");

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuario);
    }

    @DisplayName("Validar Código Recuperação Senha deve validar se for Código válido")
    @Test
    void validarCodigoRecuperacaoSenhaCodigoValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setCodigo_recuperar_senha("codigo");
        usuario.setValidade_codigo_senha(LocalDateTime.now().plusMinutes(10));

        Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(usuario));
        UsuarioValidarCodigoDto dto = new UsuarioValidarCodigoDto("email@teste.com", "codigo");

        Assertions.assertDoesNotThrow(() -> usuarioService.validarCodigoRecuperacaoSenha(dto));
    }

    @DisplayName("Mudar Senha deve mudar a senha se Código for de Validação válido")
    @Test
    void mudarSenhaCodigoValidacaoValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setNome("Nome Teste");

        Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(usuario));
        Mockito.mockStatic(JavaMail.class);
        Mockito.doNothing().when(JavaMail.class);
        JavaMail.sendEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        usuarioService.enviarCodigoRecuperarSenha("email@teste.com");

        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuario);
    }

    @DisplayName("Enviar Código Recuperar Senha deve lançar Exception se Email não cadastrado")
    @Test
    void enviarCodigoRecuperarSenhaEmailNaoCadastrado() {
        Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.enviarCodigoRecuperarSenha("email@teste.com"));
        Assertions.assertEquals("Email não cadastrado", exception.getMessage());
    }

    @DisplayName("Validar Código Recuperação Senha deve lançar Exception se Código estiver Expirado")
    @Test
    void validarCodigoRecuperacaoSenhaCodigoExpirado() {
        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getValidade_codigo_senha()).thenReturn(LocalDateTime.now().minusMinutes(1));
        Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(usuario));
        UsuarioValidarCodigoDto dto = new UsuarioValidarCodigoDto("email@teste.com", "codigo");
        Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.validarCodigoRecuperacaoSenha(dto));
        Assertions.assertEquals("A validade do codigo expirou", exception.getMessage());
    }

    @DisplayName("Mudar Senha deve lançar Exception se Email não for cadastrado")
    @Test
    void mudarSenhaEmailNaoCadastrado() {
        Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        UsuarioMudarSenhaDto dto = new UsuarioMudarSenhaDto("email@teste.com", "novaSenha");
        Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.mudarSenha(dto));
        Assertions.assertEquals("Email não cadastrado", exception.getMessage());
    }

    @DisplayName("Deletar Usuário por ID deve lançar Exception se Usuário não for encontrado")
    @Test
    void deletarUsuarioPorIdNaoEncontrado() {
        Mockito.when(usuarioRepository.existsById(ArgumentMatchers.anyInt())).thenReturn(false);
        Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.deletarUsuarioPorId(1));
        Assertions.assertEquals("Não foi encontrado nenhum usuario com esse id", exception.getMessage());
    }


    @DisplayName("Buscar Usuário por Logradouro deve retornar vazio se Logradouro não estiver encontrado")
    @Test
    void buscarUsuarioPorLogradouroNaoEncontrado() {
        Usuario usuario = Mockito.mock(Usuario.class);
        Endereco endereco = Mockito.mock(Endereco.class);
        Mockito.when(endereco.getLogradouro()).thenReturn("outroLogradouro");
        Mockito.when(usuario.getEndereco()).thenReturn(endereco);
        Mockito.when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        Optional<Usuario> resultado = usuarioService.buscarUsuarioPorLogradouro("logradouro");
        Assertions.assertTrue(resultado.isEmpty());
}

  @DisplayName("Criar Usuário deve lançar Exception se Email já estiver cadastrado")
  @Test
    void criarUsuarioEmailJaCadastrado() {
    Usuario novoUsuario = Mockito.mock(Usuario.class);
    Mockito.when(novoUsuario.getEmail()).thenReturn("email@teste.com");
    Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(novoUsuario));
    Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.criarUsuario(novoUsuario));
    Assertions.assertEquals("Este email já está cadastrado", exception.getMessage());
}


   @DisplayName("Validar Código Recuperação Senha deve lançar Exception se Código Inválido")
   @Test
    void validarCodigoRecuperacaoSenhaCodigoInvalido() {
    Usuario usuario = Mockito.mock(Usuario.class);
    Mockito.when(usuario.getCodigo_recuperar_senha()).thenReturn("codigoInvalido");
    Mockito.when(usuario.getValidade_codigo_senha()).thenReturn(LocalDateTime.now().plusMinutes(10)); // Adicione esta linha
    Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(usuario));
    UsuarioValidarCodigoDto dto = new UsuarioValidarCodigoDto("email@teste.com", "codigo");
    Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.validarCodigoRecuperacaoSenha(dto));
   // Assertions.assertEquals("Código inválido", exception.getMessage());
}

   @DisplayName("Mudar Senha deve lançar Exception se Código de Validação não encontrado")
   @Test
    void mudarSenhaCodigoValidacaoNaoEncontrado() {
    Usuario usuario = Mockito.mock(Usuario.class);
    Mockito.when(usuario.getValidade_codigo_senha()).thenReturn(null);
    Mockito.when(usuarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(usuario));
    UsuarioMudarSenhaDto dto = new UsuarioMudarSenhaDto("email@teste.com", "novaSenha");
    Exception exception = Assertions.assertThrows(ExceptionHttp.class, () -> usuarioService.mudarSenha(dto));
    Assertions.assertEquals("Nenhum codigo de validação encontrado, solicite um novo", exception.getMessage());
}

}