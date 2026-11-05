package com.uam.gestionacademica.infrastructure.persistence.mapper;

import com.uam.gestionacademica.domain.model.Estudiante;
import com.uam.gestionacademica.infrastructure.persistence.entity.CarreraEntity;
import com.uam.gestionacademica.infrastructure.persistence.entity.EstudianteEntity;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    public EstudianteEntity toEntity(Estudiante estudiante, CarreraEntity carrera) {
        if(estudiante == null) {
            return null;
        }

        return EstudianteEntity.builder()
                .id(estudiante.getId())
                .nombre(estudiante.getNombre())
                .correo(estudiante.getCorreo())
                .edad(estudiante.getEdad())
                .carrera(carrera)
                .build();
    }

    public Estudiante toDomain(EstudianteEntity entity){
        if(entity == null) {
            return null;
        }

        return new Estudiante(
                entity.getId(),
                entity.getNombre(),
                entity.getCorreo(),
                entity.getEdad(),
                entity.getCarrera().getId()
        );
    }
}
