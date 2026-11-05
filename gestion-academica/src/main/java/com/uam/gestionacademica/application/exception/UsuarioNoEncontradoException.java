package com.uam.gestionacademica.application.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(Long id) {
        super("usuario con id " + id  + " no encontrado");
    }

    public UsuarioNoEncontradoException(String username)
    {
        super("usuario con username " + username + " no encontrado");
    }
}
