package com.uam.gestionacademica.infrastructure.config;

import com.uam.gestionacademica.application.port.out.UsuarioRepositoryPort;
import com.uam.gestionacademica.application.service.UsuarioService;
import com.uam.gestionacademica.domain.model.Rol;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;



@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final UsuarioService usuarioService;
    private final String adminUsername;
    private final String adminPassword;
    private final String adminNombre;

    public DataInitializer(UsuarioRepositoryPort usuarioRepositoryPort, UsuarioService usuarioService,
                           @Value("${app.admin.username}") String adminUsername,
                           @Value("${app.admin.password}") String adminPassword,
                           @Value("${app.admin.nombre}") String adminNombre) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.usuarioService = usuarioService;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminNombre = adminNombre;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(usuarioRepositoryPort.existsByRol(Rol.ADMIN)){
            return;
        }

        if(adminPassword == null || adminPassword.length() < 8){
            log.warn("No existe ningún ADMIN y ADMIN_PASSWORD no está definida (mínimo 8 caracteres)." +
                    "No se creó el administrador inicial");
            return;
        }

        usuarioService.createUsuario(adminUsername, adminPassword, adminNombre, Rol.ADMIN);
        log.info("Usuario administrador inicial '{}' creado", adminUsername);
    }
}
