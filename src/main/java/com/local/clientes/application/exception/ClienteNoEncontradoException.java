package com.local.clientes.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class ClienteNoEncontradoException extends EntityNotFoundException {
    public ClienteNoEncontradoException(String message) {
        super(message);
    }
}