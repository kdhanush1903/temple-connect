package com.cousincodelabs.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;


    private byte[] getSigningKey() {
        return jwtSecret.getBytes(StandardCharsets.UTF_8);
    }

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Method to generate a JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SECRET_KEY)  // Use the generated key here
                .compact();
    }


    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey( getSigningKey()).build().parseSignedClaims(token).getBody();
    }

    public boolean validateToken(String token, String username) {
        String tokenUsername = getClaimsFromToken(token).getSubject();
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}


