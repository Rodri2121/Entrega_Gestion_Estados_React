package com.uam.gestionacademica.infrastructure.persistence;

import com.uam.gestionacademica.application.port.out.UsuarioRepositoryPort;
import com.uam.gestionacademica.domain.model.Rol;
import com.uam.gestionacademica.domain.model.Usuario;
import com.uam.gestionacademica.infrastructure.persistence.entity.UsuarioEntity;
import com.uam.gestionacademica.infrastructure.persistence.mapper.UsuarioMapper;
import com.uam.gestionacademica.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioPersistenceAdapter(UsuarioJpaRepository usuarioJpaRepository, UsuarioMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = usuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = usuarioJpaRepository.save(entity);
        return usuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioJpaRepository.findById(id)
                .map(usuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioJpaRepository.findByUsername(username)
                .map(usuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioJpaRepository.findAll()
                .stream()
                .map(usuarioMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return usuarioJpaRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return usuarioJpaRepository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public boolean existsByRol(Rol rol) {
        return usuarioJpaRepository.existsByRol(rol);
    }

    @Override
    public void deleteById(Long id) {
        usuarioJpaRepository.deleteById(id);
    }
}
