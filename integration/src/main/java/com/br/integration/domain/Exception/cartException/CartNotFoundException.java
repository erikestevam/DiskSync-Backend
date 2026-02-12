package com.br.integration.domain.Exception.cartException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String email) {
        super("Carrinho não encontrado para o usuário: " + email);
    }
}


