package com.uam.gestionacademica.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret,
        Duration expiration,
        String issuer
) {
    private static final int MIN_SECRET_BYTES=32;

    public JwtProperties {
        if(secret == null || secret.isBlank()){
            throw new IllegalStateException("Falta security.jwt.secret");
        }

        byte[] keyBytes;
        try{
            keyBytes = Base64.getDecoder().decode(secret);
        }
        catch (IllegalArgumentException e){
            throw new IllegalStateException("security.jwt.secret debe estar en Base64");
        }

        if(keyBytes.length<MIN_SECRET_BYTES){
            throw new IllegalStateException("security.jwt.secret debe tener al menos 32 bytes (256 bits)");
        }

        if(expiration == null){
            expiration = Duration.ofHours(1);
        }

        if(issuer == null){
            issuer = "gestion-academica";
        }


    }

    public SecretKey secretKey(){
        return new SecretKeySpec(Base64.getDecoder().decode(secret),"HmacSHA256");
    }
}
