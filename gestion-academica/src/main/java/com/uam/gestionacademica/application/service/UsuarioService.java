package com.uam.gestionacademica.application.service;

import com.uam.gestionacademica.application.exception.OperacionNoPermitidaException;
import com.uam.gestionacademica.application.exception.UsernameYaRegistradoException;
import com.uam.gestionacademica.application.exception.UsuarioNoEncontradoException;
import com.uam.gestionacademica.application.port.out.PasswordEncoderPort;
import com.uam.gestionacademica.application.port.out.UsuarioRepositoryPort;
import com.uam.gestionacademica.domain.model.Rol;
import com.uam.gestionacademica.domain.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoderPort passwordEncoderPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    /*
    * Caso de uso: Crear usuario
    * La contraseña se guarda siempre como hash
    */
    @Transactional
    public Usuario createUsuario(String username, String password, String nombre, Rol rol){
        String normalizedUsername = Usuario.normalizeUsername(username);

        if(usuarioRepositoryPort.existsByUsername(normalizedUsername)){
            throw new UsernameYaRegistradoException(normalizedUsername);
        }

        Usuario usuario = new Usuario(
                null,
                normalizedUsername,
                passwordEncoderPort.encode(password),
                nombre,
                rol,
                true
        );

        return usuarioRepositoryPort.save(usuario);
    }

    /*
    * Caso de uso: Obtener usuario por ID
    */
    @Transactional(readOnly = true)
    public Usuario getUsuarioById(Long id){
        return usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    /*
    * Caso de uso: Obtener usuario por username
    */
    @Transactional(readOnly = true)
    public Usuario getUsuarioByUsername(String username){
        String normalizedUsername = Usuario.normalizeUsername(username);
        return usuarioRepositoryPort.findByUsername(normalizedUsername)
                .orElseThrow(() -> new UsuarioNoEncontradoException(normalizedUsername));
    }

    /*
    * Caso de uso: Listar usuarios
    */
    @Transactional(readOnly = true)
    public List<Usuario> getAllUsuarios(){
        return usuarioRepositoryPort.findAll();
    }

    /*
    * Caso de uso: Actualizar datos de un usuario (no cambia contraseña)
    * Un administrador no puede quitarse su propio Rol ADMIN ni desactivarse
    */
    @Transactional
    public Usuario updateUsuario(Long id, String username, String nombre, Rol rol,
                                 boolean activo, String currentUsername){
        Usuario existente = getUsuarioById(id);
        String normalizedUsername = Usuario.normalizeUsername(username);

        if(usuarioRepositoryPort.existsByUsernameAndIdNot(normalizedUsername, id)){
            throw new UsernameYaRegistradoException(normalizedUsername);
        }

        boolean esSuPropiaCuenta = existente.getUsername().equals(Usuario.normalizeUsername(currentUsername));
        if(esSuPropiaCuenta && (rol != Rol.ADMIN || !activo)){
            throw new OperacionNoPermitidaException("No puedes quitarse el rol de ADMIN ni desactivar tu propia cuenta");
        }

        Usuario actualizado = new Usuario(
                id,
                normalizedUsername,
                existente.getPasswordHash(),
                nombre,
                rol,
                activo
        );

        return usuarioRepositoryPort.save(actualizado);
    }

    /*
    * Caso de uso: Cambiar contraseña de un usuario
    */
    @Transactional
    public void changePassword(Long id, String newRawPassword){
        Usuario existente = getUsuarioById(id);

        Usuario actualizado = new Usuario(
                existente.getId(),
                existente.getUsername(),
                passwordEncoderPort.encode(newRawPassword),
                existente.getNombre(),
                existente.getRol(),
                existente.isActivo()
        );

        usuarioRepositoryPort.save(actualizado);
    }

    /*
    * Caso de uso: Eliminar usuario
    * Un administrador no puede eliminar su propia cuenta
    */
    @Transactional
    public void deleteUsuario(Long id, String currentUsername){
        Usuario existente = getUsuarioById(id);

        if(existente.getUsername().equals(Usuario.normalizeUsername(currentUsername))){
            throw  new OperacionNoPermitidaException("No puedes eliminar tu propia cuenta");
        }

        usuarioRepositoryPort.deleteById(id);
    }
}
