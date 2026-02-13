package com.br.integration.domain.exception.cartException;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String email) {
        super("Carrinho não encontrado para o usuário: " + email);
    }
}


