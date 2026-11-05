package com.uam.gestionacademica.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
* DTO de entrada para actualizar un estudiante
* No incluye ID porque el ID viene en la ruta:
* PUT /api/v1/estudiantes/{id}
*
* Esto evita que el cliente intente cambiar el ID
* enviándolo dentro del body
* */
public record EstudianteUpdateRequest(
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
