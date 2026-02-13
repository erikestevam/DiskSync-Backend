package com.br.integration.domain.exception.orderException;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super("Transição de estado inválida: " + message);
    }
}
