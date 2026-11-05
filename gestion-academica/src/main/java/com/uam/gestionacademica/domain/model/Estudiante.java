package com.uam.gestionacademica.domain.model;

import com.uam.gestionacademica.domain.exception.EstudianteDebeSerMayorEdadException;

public class Estudiante {
    private final Long id;
    private final String nombre;
    private final String correo;
    private final Integer edad;
    private final Long carreraId;

    public Estudiante(Long id, String nombre, String correo, Integer edad, Long carreraId) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.edad = edad;
        this.carreraId = carreraId;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public Integer getEdad() {
        return edad;
    }

    public Long getCarreraId() {
        return carreraId;
    }

    public boolean isMayorDeEdad(){
        return edad != null && edad >= 18;
    }

    /*
        Validación de negocio
        Esta regla pertenece al dominio
    * */
    public void validateCanRegister(){
        if(!isMayorDeEdad()){
            throw new EstudianteDebeSerMayorEdadException("El estudiante debe ser mayor de edad para registrarse");

        }
    }
}
