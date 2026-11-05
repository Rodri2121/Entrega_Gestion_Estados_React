package com.uam.gestionacademica.domain.model;

public class Carrera {
    private final Long id;
    private final String codigo;
    private final String nombre;

    public Carrera(Long id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }
}
