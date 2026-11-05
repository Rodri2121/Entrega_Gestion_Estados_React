package com.uam.gestionacademica.application.port.out;

import com.uam.gestionacademica.domain.model.Carrera;

import java.util.List;
import java.util.Optional;

public interface CarreraRepositoryPort {
    Carrera save(Carrera carrera);
    Optional<Carrera> findById(Long id);
    List<Carrera> findAll();
    boolean existsById(Long id);
    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndIdNot(String codigo, Long id);
    void deleteById(Long id);
}
