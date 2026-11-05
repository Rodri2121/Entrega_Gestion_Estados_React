package com.uam.gestionacademica.infrastructure.persistence.repository;

import com.uam.gestionacademica.infrastructure.persistence.entity.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteJpaRepository extends JpaRepository<EstudianteEntity, Long> {
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, Long id);

    /* carrera.id -> se navega la relacion con "_" */
    boolean existsByCarrera_Id(Long carreraId);
}
