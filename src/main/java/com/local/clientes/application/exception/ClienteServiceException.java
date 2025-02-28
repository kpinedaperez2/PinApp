package com.local.clientes.application.exception;

public class ClienteServiceException extends RuntimeException {
    public ClienteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
