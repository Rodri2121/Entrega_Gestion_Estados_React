package com.uam.gestionacademica.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


/*
 * Se ejecuta cuando la petición NO está autenticada:
 * - no envió token
 * - token con firma inválida, expirado o con issuer incorrecto
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = (authException instanceof InvalidBearerTokenException)
                ? "Token inválido o expirado"
                : "Se requiere autenticación";

        response.setHeader(HttpHeaders.WWW_AUTHENTICATE,"Bearer");
        SecurityErrorResponseWriter.write(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message);
    }
}
