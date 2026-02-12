package com.br.integration.domain.Exception.orderException;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Pedido não encontrado com id: " + id);
    }
}
