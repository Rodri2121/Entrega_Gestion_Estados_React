package com.uam.gestionacademica.infrastructure.web.dto;

import com.uam.gestionacademica.domain.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
        String username,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,
        @NotNull(message = "El rol es obligatorio")
        Rol rol,
        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo
) {
}
