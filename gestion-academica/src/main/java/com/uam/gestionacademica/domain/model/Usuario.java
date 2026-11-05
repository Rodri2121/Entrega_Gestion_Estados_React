package com.uam.gestionacademica.domain.model;

import com.uam.gestionacademica.domain.exception.UsuarioInactivoException;

import java.util.Locale;

public class Usuario {
    private final Long id;
    private final String username;
    private final String passwordHash;
    private final String nombre;
    private final Rol rol;
    private final boolean activo;

    public Usuario(Long id, String username, String passwordHash, String nombre, Rol rol, boolean activo) {
        this.id = id;
        this.username = normalizeUsername(username);
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getNombre() {
        return nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    /* Regla de dominio: los usernames no distinguen mayúsculas
    * ni espacios al inicio o al final */
    public static String normalizeUsername(String username){
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }

    /*
    * Regla de dominio: un usuario inactivo no puede iniciar sesión
    * */
    public void validateCanLogin(){
        if(!activo){
            throw new UsuarioInactivoException("El usuario está inactivo");
        }
    }

}
