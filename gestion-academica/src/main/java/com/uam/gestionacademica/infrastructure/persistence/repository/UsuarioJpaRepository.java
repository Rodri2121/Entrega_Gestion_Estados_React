package com.uam.gestionacademica.infrastructure.persistence.repository;

import com.uam.gestionacademica.domain.model.Rol;
import com.uam.gestionacademica.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByRol(Rol rol);
}
