package com.uam.gestionacademica.infrastructure.security;

import com.uam.gestionacademica.application.port.out.TokenProvider;
import com.uam.gestionacademica.domain.model.Usuario;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/*
* Implementación JWT del puerto TokenProviderPort
* Claims emitidos:
* -sub   : username
* -uid   : id del usuario
* -roles : ["ADMIN"](se convierte en ROLE_ADMIN al validar)
* -iss, iat, exp
*/
@Component
public class JwtTokenProvider implements TokenProvider {
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.expiration()))
                .subject(usuario.getUsername())
                .claim("uid", usuario.getId())
                .claim(SecurityConfig.ROLES_CLAIM, List.of(usuario.getRol().name()))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public long getExpirationSeconds() {
        return jwtProperties.expiration().toSeconds();
    }
}
