package com.cousincodelabs.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, getSigningKey())
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


