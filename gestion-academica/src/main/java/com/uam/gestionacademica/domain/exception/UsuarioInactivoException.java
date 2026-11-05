package com.uam.gestionacademica.domain.exception;

public class UsuarioInactivoException extends RuntimeException {
    public UsuarioInactivoException(String message) {
        super(message);
    }
}
