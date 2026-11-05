package com.uam.gestionacademica.application.port.out;

import com.uam.gestionacademica.domain.model.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepositoryPort {
    Estudiante save(Estudiante estudiante);
    Optional<Estudiante> findById(Long id);
    List<Estudiante> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, Long id);
    boolean existsByCarreraId(Long carreraId);
}
