package com.uam.gestionacademica.application.exception;

public class EstudianteNoEncontradoException extends RuntimeException{
    public EstudianteNoEncontradoException(Long id){
        super("Estudiante con id " + id + " no encontrado");
    }
}
