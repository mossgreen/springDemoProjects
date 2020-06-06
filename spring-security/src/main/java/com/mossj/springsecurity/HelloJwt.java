package com.mossj.springsecurity;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class HelloJwt {


    @Test
    public void generate() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        final String encoded = Base64.getEncoder().encodeToString(key.getEncoded());
        log.info("encoded Key: {}", encoded);

        final SecretKey secretKey = Keys.hmacShaKeyFor(encoded.getBytes());
        var token = Jwts.builder().setSubject("moss").signWith(secretKey).compact();
        log.info("token: {}", token);

        final String subject = Jwts.parserBuilder()
            .setSigningKey(secretKey).build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
        log.info("sub: {}",subject);

        assertEquals(subject, "moss");
    }
}
