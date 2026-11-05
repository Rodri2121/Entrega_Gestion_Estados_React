package com.uam.gestionacademica.infrastructure.persistence.mapper;

import com.uam.gestionacademica.domain.model.Carrera;
import com.uam.gestionacademica.infrastructure.persistence.entity.CarreraEntity;
import org.springframework.stereotype.Component;

@Component
public class CarreraMapper {
    public CarreraEntity toEntity(Carrera carrera){
        if(carrera == null){
            return null;
        }

        return CarreraEntity.builder()
                .id(carrera.getId())
                .codigo(carrera.getCodigo())
                .nombre(carrera.getNombre())
                .build();
    }

    public Carrera toDomain(CarreraEntity entity){
        if(entity == null){
            return null;
        }

        return new Carrera(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre()
        );
    }
}
