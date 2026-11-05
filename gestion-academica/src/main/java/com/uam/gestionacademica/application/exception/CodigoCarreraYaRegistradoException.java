package com.uam.gestionacademica.application.exception;

public class CodigoCarreraYaRegistradoException extends RuntimeException {
    public CodigoCarreraYaRegistradoException(String codigo) {
        super("El código de carrera " + codigo + " ya esta registrado");
    }
}
