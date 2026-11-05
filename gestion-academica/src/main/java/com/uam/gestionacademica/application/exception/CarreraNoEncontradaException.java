package com.uam.gestionacademica.application.exception;

public class CarreraNoEncontradaException extends RuntimeException {
    public CarreraNoEncontradaException(Long id) {

        super("Carrera con id " + id + " no encontrada");
    }
}
