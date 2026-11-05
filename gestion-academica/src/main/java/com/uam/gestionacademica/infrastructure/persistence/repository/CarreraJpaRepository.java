package com.uam.gestionacademica.infrastructure.persistence.repository;

import com.uam.gestionacademica.infrastructure.persistence.entity.CarreraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraJpaRepository extends JpaRepository<CarreraEntity, Long> {
    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
