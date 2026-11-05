package com.uam.gestionacademica.application.model;

import com.uam.gestionacademica.domain.model.Rol;

public record LoginResult(String accessToken, long expiresIn, String username, Rol rol) {
}
