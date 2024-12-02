package com.api.jesus_king_tech.api.security;

import com.api.jesus_king_tech.domain.usuario.autenticacao.AutenticacaoService;
import com.api.jesus_king_tech.util.PasswordUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AutenticacaoProvider implements AuthenticationProvider {

    private final AutenticacaoService usuarioAutorizacaoService;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoProvider(
            AutenticacaoService usuarioAutorizacaoService,
            PasswordEncoder passwordEncoder
    ){
        this.usuarioAutorizacaoService =usuarioAutorizacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserDetails userDetails = this.usuarioAutorizacaoService.loadUserByUsername(username);

        if (this.passwordEncoder.matches(password, userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Usuario ou senha invárlidos");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
