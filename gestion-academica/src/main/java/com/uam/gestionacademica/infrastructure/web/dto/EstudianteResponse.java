package com.uam.gestionacademica.infrastructure.web.dto;

/*
* DTO de salida
* Simple, inmutable y sin lógica
* Es la representación JSON que se envía al cliente
* */
public record EstudianteResponse(
        Long id,
        String nombre,
        String correo,
        Integer edad,
        Long carreraId
) {
}
