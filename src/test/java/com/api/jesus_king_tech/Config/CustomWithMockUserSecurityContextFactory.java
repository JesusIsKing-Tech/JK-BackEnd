package com.api.jesus_king_tech.Config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomWithMockUserSecurityContextFactory implements WithSecurityContextFactory<CustomWithMockUser> {

    @Override
    public SecurityContext createSecurityContext(CustomWithMockUser annotation) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        var authorities = Arrays.stream(annotation.authorities())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        var authentication = new UsernamePasswordAuthenticationToken(
                annotation.username(),
                annotation.password(),
                authorities
        );

        Map<String, String> authenticationDetails = new HashMap<>();
        authenticationDetails.put("userEmail", annotation.username());
        authentication.setDetails(authenticationDetails);

        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}



