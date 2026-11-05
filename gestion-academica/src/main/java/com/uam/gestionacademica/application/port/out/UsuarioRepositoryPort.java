package com.uam.gestionacademica.application.port.out;

import com.uam.gestionacademica.domain.model.Rol;
import com.uam.gestionacademica.domain.model.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepositoryPort {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findAll();
    boolean existsById(Long id);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByRol(Rol rol);
    void deleteById(Long id);
}
