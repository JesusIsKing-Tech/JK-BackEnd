package com.api.jesus_king_tech.api.security;

import com.api.jesus_king_tech.api.security.jwt.GerenciadorTokenJwt;
import com.api.jesus_king_tech.domain.usuario.autenticacao.AutenticacaoService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class AutenticacaoFilter extends OncePerRequestFilter {

//    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AutenticacaoFilter.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacaoFilter.class);

    private final AutenticacaoService autenticacaoService;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public AutenticacaoFilter(
            AutenticacaoService autenticacaoService,
            GerenciadorTokenJwt gerenciadorTokenJwt
    ) {
        this.autenticacaoService = autenticacaoService;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String username = null;
        String jwtToken = null;

        String requestTokenHeader = request.getHeader("Authorization");

        if (Objects.nonNull(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")){
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = gerenciadorTokenJwt.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException exception){

                LOGGER.info("[FALHA AUTENTICAÇÃO] - Token expirado, usuario:{} - {}"
                        , exception.getClaims().getSubject(), exception.getMessage());

                LOGGER.trace("[FALHA AUTENTICAÇÃO] - stack trace: %s", exception);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            addUsernameInContext(request, username, jwtToken);
        }
        filterChain.doFilter(request, response);
    }

    private void addUsernameInContext(HttpServletRequest request, String username, String jwtToken){
        UserDetails userDetails = autenticacaoService.loadUserByUsername(username);

        if (gerenciadorTokenJwt.validateToken(jwtToken, userDetails)){

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

    }
}
