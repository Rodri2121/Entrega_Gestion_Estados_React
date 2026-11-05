package com.uam.gestionacademica.infrastructure.web;

import com.uam.gestionacademica.application.service.UsuarioService;
import com.uam.gestionacademica.domain.model.Usuario;
import com.uam.gestionacademica.infrastructure.web.dto.CambioPasswordRequest;
import com.uam.gestionacademica.infrastructure.web.dto.UsuarioRequest;
import com.uam.gestionacademica.infrastructure.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request){
        Usuario created = usuarioService.createUsuario(
                request.username(),
                request.password(),
                request.nombre(),
                request.rol()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UsuarioResponse.fromDomain(created));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAll(){
        List<UsuarioResponse> usuarios = usuarioService.getAllUsuarios()
                .stream()
                .map(UsuarioResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(UsuarioResponse.fromDomain(usuarioService.getUsuarioById(id)));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody CambioPasswordRequest request){
        usuarioService.changePassword(id, request.password());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal Jwt jwt){
        usuarioService.deleteUsuario(id, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

}
