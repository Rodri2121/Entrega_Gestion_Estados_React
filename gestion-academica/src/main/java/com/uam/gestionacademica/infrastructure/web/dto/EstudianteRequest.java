package com.uam.gestionacademica.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
* DTO de entrada para crear un estudiante
* Las validaciones aquí son técnicas
* - formato
* - obligatoriedad
*
* No reemplazan las reglas de negocio del dominio
* */
public record EstudianteRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo es inválido")
        String correo,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 1, message = "La edad debe ser positiva")
        Integer edad,

        @NotNull(message = "La carrera es obligatoria")
        Long carrera
) {
}
