package com.uam.gestionacademica.application.exception;

public class CarreraConEstudiantesException extends RuntimeException {
    public CarreraConEstudiantesException(Long id) {
        super("No se puede eliminar la carrera con id " +id + " porque tiene estudiantes asociados");
    }
}
