package com.br.integration.domain.service;

import com.br.integration.domain.Exception.authException.UserNotAuthenticatedException;
import com.br.integration.domain.entites.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public String getCurrentUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user.getEmail();
        }

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails details) {
            return details.getUsername();
        }

        throw new UserNotAuthenticatedException("Não foi possível identificar o usuário logado.");
    }
}
