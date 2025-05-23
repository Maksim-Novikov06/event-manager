package com.paradise.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {


    private final SecretKey key;

    private final long expirationTime;

    public JwtTokenManager(
            @Value("${jwt.secret}") String key,
            @Value("${jwt.expiration}") long expirationTime) {

        this.key = Keys.hmacShaKeyFor(key.getBytes());
        this.expirationTime = expirationTime;
    }


    public String generateToken(Long userId, String login){

        return Jwts
                .builder()
                .subject(login)
                .claim("id", userId)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();

    }

    public String getLoginFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Long getUserIdFromToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }
}
