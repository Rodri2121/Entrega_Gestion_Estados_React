package com.uam.gestionacademica.application.exception;

public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String correo) {
        super("El correo " + correo + " ya está registrado");

    }
}
