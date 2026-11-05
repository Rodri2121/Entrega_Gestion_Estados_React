package com.uam.gestionacademica.infrastructure.web;

import com.uam.gestionacademica.application.exception.*;
import com.uam.gestionacademica.domain.exception.EstudianteDebeSerMayorEdadException;
import com.uam.gestionacademica.domain.exception.UsuarioInactivoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
* Traduce excepciones a respuestas HTTP con formato JSOn uniforme.
*
* IMPORTANTE: no agregar un handler genérico a Exception.class
* porque capturaria también excepciones de spring security
*/
@RestControllerAdvice
public class GlobalExceptionHandler {
    // -------------- 404 NOT FOUND ----------------------------------
    @ExceptionHandler({
            EstudianteNoEncontradoException.class,
            CarreraNoEncontradaException.class,
            UsuarioNoEncontradoException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(
            RuntimeException ex
    ){
        return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // -------------- 400 Bad Request ----------------------------------
    @ExceptionHandler(EstudianteDebeSerMayorEdadException.class)
    public ResponseEntity<Map<String, Object>> handleEstudianteDebeSerMayorEdad(
            EstudianteDebeSerMayorEdadException e
    ){
        return buildError(HttpStatus.BAD_REQUEST, "Business Rule Violation", e.getMessage());
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<Map<String, Object>> handleOperacionNoPermitida(
            OperacionNoPermitidaException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, "Business Rule Violation", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex
    ) {
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", "El cuerpo de la petición es inválido o tiene un" +
                "formato incorrecto");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e){
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request",
                "El parámetro '" + e.getName() + "' tiene un valor inválido");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        fieldErrors.put(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Error");
        error.put("errors", fieldErrors);

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    // ---------- 401 / 403 ------------------
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, Object>> handleCredencialesInvalidas(CredencialesInvalidasException e){
        return buildError(HttpStatus.UNAUTHORIZED, "Unauthorized", e.getMessage());
    }

    @ExceptionHandler(UsuarioInactivoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioInactivo(UsuarioInactivoException e){
        return buildError(HttpStatus.FORBIDDEN, "Forbidden", e.getMessage());
    }

    /*
    * @PreAuthorize lanza esta excepción dentro del controller
    * por eso llega aquí y no al RestAccesDeniedHandler
    */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException e){
        return buildError(HttpStatus.FORBIDDEN, "Forbidden",
                "No tiene permisos para realizar esta operacion");
    }

    // ------------ 409 Conflict --------------------
    @ExceptionHandler({
            CorreoYaRegistradoException.class,
            CodigoCarreraYaRegistradoException.class,
            UsernameYaRegistradoException.class,
            CarreraConEstudiantesException.class
    })
    public ResponseEntity<Map<String, Object>> handleCorreoYaRegistrado(RuntimeException e){
        return buildError(HttpStatus.CONFLICT, "Conflict", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException e){
        return buildError(HttpStatus.CONFLICT, "Constraint Violation",
                "Se violó una restricción de integridad de datos");
    }

    // ------------ Helper ------------------------
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String error, String message){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}
