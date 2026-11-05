package com.uam.gestionacademica.infrastructure.persistence.entity;

import com.uam.gestionacademica.domain.model.Rol;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    /* Hash BCrypt (60 caracteres). Nunca texto plano*/
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;
    @Column(nullable = false)
    private boolean activo;
}
