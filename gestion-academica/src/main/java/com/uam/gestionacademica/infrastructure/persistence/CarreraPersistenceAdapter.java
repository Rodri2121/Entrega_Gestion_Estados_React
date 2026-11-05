package com.uam.gestionacademica.infrastructure.persistence;

import com.uam.gestionacademica.application.port.out.CarreraRepositoryPort;
import com.uam.gestionacademica.domain.model.Carrera;
import com.uam.gestionacademica.infrastructure.persistence.entity.CarreraEntity;
import com.uam.gestionacademica.infrastructure.persistence.mapper.CarreraMapper;
import com.uam.gestionacademica.infrastructure.persistence.repository.CarreraJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CarreraPersistenceAdapter implements CarreraRepositoryPort {
    private final CarreraJpaRepository carreraJpaRepository;
    private final CarreraMapper carreraMapper;

    public CarreraPersistenceAdapter(CarreraJpaRepository carreraJpaRepository,
                                        CarreraMapper carreraMapper) {
        this.carreraJpaRepository = carreraJpaRepository;
        this.carreraMapper = carreraMapper;
    }

    @Override
    public Carrera save(Carrera carrera) {
        CarreraEntity entity = carreraMapper.toEntity(carrera);
        CarreraEntity savedEntity = carreraJpaRepository.save(entity);

        return carreraMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Carrera> findById(Long id) {
        return carreraJpaRepository.findById(id)
                .map(carreraMapper::toDomain);
    }

    @Override
    public List<Carrera> findAll() {
        return carreraJpaRepository
                .findAll()
                .stream()
                .map(carreraMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return carreraJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return carreraJpaRepository.existsByCodigo(codigo);
    }

    @Override
    public boolean existsByCodigoAndIdNot(String codigo, Long id) {
        return carreraJpaRepository.existsByCodigoAndIdNot(codigo, id);
    }

    @Override
    public void deleteById(Long id) {
        carreraJpaRepository.deleteById(id);
    }
}
