package com.uam.gestionacademica.application.port.out;

import com.uam.gestionacademica.domain.model.Usuario;

/*
* Abstrae la generación de tokens de acceso
* */
public interface TokenProvider {
    String generateToken(Usuario usuario);
    long getExpirationSeconds();
}
