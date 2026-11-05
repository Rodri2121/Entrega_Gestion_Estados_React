package com.uam.gestionacademica.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * Se ejecuta cuando el usuario SÍ está autenticado pero su rol no tiene permiso.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecurityErrorResponseWriter.write(
                response,
                HttpStatus.FORBIDDEN,
                "Forbidden",
                "No tiene permisos para realizar esta operación"
        );
    }
}
