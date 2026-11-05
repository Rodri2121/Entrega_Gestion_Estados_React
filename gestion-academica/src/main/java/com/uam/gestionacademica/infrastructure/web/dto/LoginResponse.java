package com.uam.gestionacademica.infrastructure.web.dto;

import com.uam.gestionacademica.domain.model.Rol;

/*
* expiresIn: segundos de vida del token.
* El cliente debe enviarlo en cada peticion:
* Authorization: Bearer<accessToken>
*/
public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String username,
        Rol rol
) {
}
