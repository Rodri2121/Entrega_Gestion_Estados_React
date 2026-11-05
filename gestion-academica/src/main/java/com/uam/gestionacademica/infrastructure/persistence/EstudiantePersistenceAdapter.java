package com.uam.gestionacademica.infrastructure.persistence;

/*
* Adaptador de persistencia
* Implementa el puerto definido en la capa de aplicación.
*
* Este es el único lugar donde se conectan:
* - Spring Data JPA
* - PostgreSQL
*
* El servicio de aplicación solo conoce la interfaz
*
* */

import com.uam.gestionacademica.application.port.out.EstudianteRepositoryPort;
import com.uam.gestionacademica.domain.model.Estudiante;
import com.uam.gestionacademica.infrastructure.persistence.entity.CarreraEntity;
import com.uam.gestionacademica.infrastructure.persistence.entity.EstudianteEntity;
import com.uam.gestionacademica.infrastructure.persistence.mapper.EstudianteMapper;
import com.uam.gestionacademica.infrastructure.persistence.repository.CarreraJpaRepository;
import com.uam.gestionacademica.infrastructure.persistence.repository.EstudianteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EstudiantePersistenceAdapter implements EstudianteRepositoryPort {

    private final EstudianteJpaRepository estudianteJpaRepository;
    private final CarreraJpaRepository carreraJpaRepository;
    private final EstudianteMapper estudianteMapper;

    public EstudiantePersistenceAdapter(EstudianteJpaRepository estudianteJpaRepository,
                                        CarreraJpaRepository carreraJpaRepository,
                                        EstudianteMapper estudianteMapper) {
        this.estudianteJpaRepository = estudianteJpaRepository;
        this.carreraJpaRepository = carreraJpaRepository;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    public Estudiante save(Estudiante estudiante) {
        CarreraEntity carrera = carreraJpaRepository.findById(estudiante.getCarreraId())
                .orElseThrow(() -> new IllegalArgumentException("La carrera no existe"));
        EstudianteEntity entity = estudianteMapper.toEntity(estudiante, carrera);
        EstudianteEntity savedEntity = estudianteJpaRepository.save(entity);
        return estudianteMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Estudiante> findById(Long id) {
        return estudianteJpaRepository.findById(id)
                .map(estudianteMapper::toDomain);
    }

    @Override
    public List<Estudiante> findAll() {
        return estudianteJpaRepository.findAll()
                .stream()
                .map(estudianteMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        estudianteJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return estudianteJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return estudianteJpaRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existsByCorreoAndIdNot(String correo, Long id) {
        return estudianteJpaRepository.existsByCorreoAndIdNot(correo, id);
    }

    @Override
    public boolean existsByCarreraId(Long carreraId) {
        return estudianteJpaRepository.existsByCarrera_Id(carreraId);
    }
}
