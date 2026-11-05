package com.uam.gestionacademica.infrastructure.persistence.mapper;

import com.uam.gestionacademica.domain.model.Usuario;
import com.uam.gestionacademica.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(Usuario usuario){
        if(usuario == null) return null;

        return UsuarioEntity.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .build();

    }

    public Usuario toDomain(UsuarioEntity entity){
        if(entity == null) return null;

        return new Usuario(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getNombre(),
                entity.getRol(),
                entity.isActivo()
        );
    }
}
