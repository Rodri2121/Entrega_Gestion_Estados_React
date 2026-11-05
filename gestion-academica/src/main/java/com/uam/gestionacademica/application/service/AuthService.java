package com.uam.gestionacademica.application.service;

import com.uam.gestionacademica.application.exception.CredencialesInvalidasException;
import com.uam.gestionacademica.application.model.LoginResult;
import com.uam.gestionacademica.application.port.out.PasswordEncoderPort;
import com.uam.gestionacademica.application.port.out.TokenProvider;
import com.uam.gestionacademica.application.port.out.UsuarioRepositoryPort;
import com.uam.gestionacademica.domain.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProvider tokenProvider;

    public AuthService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoderPort passwordEncoderPort, TokenProvider tokenProvider) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProvider = tokenProvider;
    }

    /*
    * Caso de uso: Iniciar sesión
    * 1. Buscar usuario por username
    * 2. Verificar contraseña del hash
    * 3. Validar regla de dominio (usuario activo)
    * 4. Emitir token
    */
    @Transactional(readOnly = true)
    public LoginResult login(String username, String password) {
        Usuario usuario = usuarioRepositoryPort.findByUsername(Usuario.normalizeUsername(username))
                .orElseThrow(CredencialesInvalidasException::new);

        if(!passwordEncoderPort.matches(password, usuario.getPasswordHash())){
            throw new CredencialesInvalidasException();
        }

        usuario.validateCanLogin();

        String token = tokenProvider.generateToken(usuario);

        return new LoginResult(
                token,
                tokenProvider.getExpirationSeconds(),
                usuario.getUsername(),
                usuario.getRol()
        );
    }
}
