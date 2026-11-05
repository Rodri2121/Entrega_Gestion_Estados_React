package com.uam.gestionacademica.infrastructure.web.dto;

import com.uam.gestionacademica.domain.model.Rol;
import com.uam.gestionacademica.domain.model.Usuario;

public record UsuarioResponse(
        Long id,
        String username,
        String nombre,
        Rol rol,
        boolean activo
) {
    public static UsuarioResponse fromDomain(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getRol(),
                usuario.isActivo()
        );
    }
}
