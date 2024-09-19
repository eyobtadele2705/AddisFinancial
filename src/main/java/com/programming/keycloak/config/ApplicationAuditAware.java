package com.programming.keycloak.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
//        User principal = (User) authentication.getPrincipal();

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaim("username");

//        return Optional.ofNullable(principal.getId());
        return Optional.ofNullable(authentication.getName());
    }
}
