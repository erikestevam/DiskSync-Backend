package com.br.integration.domain.Exception.orderException;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super("Transição de estado inválida: " + message);
    }
}
