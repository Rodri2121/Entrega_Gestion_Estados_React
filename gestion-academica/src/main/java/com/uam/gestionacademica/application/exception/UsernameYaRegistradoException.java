package com.uam.gestionacademica.application.exception;

public class UsernameYaRegistradoException extends RuntimeException {
    public UsernameYaRegistradoException(String username) {
        super("El username " + username + " ya está registrado");
    }
}
