package com.uam.gestionacademica.infrastructure.web;

import com.uam.gestionacademica.application.model.LoginResult;
import com.uam.gestionacademica.application.service.AuthService;
import com.uam.gestionacademica.application.service.UsuarioService;
import com.uam.gestionacademica.infrastructure.web.dto.LoginRequest;
import com.uam.gestionacademica.infrastructure.web.dto.LoginResponse;
import com.uam.gestionacademica.infrastructure.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    /*
    * Público. Devuelve JWT.
    */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResult result = authService.login(request.username(),  request.password());

        return ResponseEntity.ok(new LoginResponse(
                result.accessToken(),
                "Bearer",
                result.expiresIn(),
                result.username(),
                result.rol()
        ));
    }

    /*
    * Requiere token. Devuelve los datos del usuario autenticado.
    * jwt.getSubject() = username (claim "sub")
    */
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(
                UsuarioResponse.fromDomain(usuarioService.getUsuarioByUsername(jwt.getSubject()))
        );
    }
}
