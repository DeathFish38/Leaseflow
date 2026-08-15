package com.leaseflow.backend.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.users.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    // generate token
    // input: User -> output: key
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.expiration()))
                .signWith(getSigningKey())
                .compact();
    }

    // extract email
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // validate token
    public boolean validateToken(String token){
        try {
            extractClaims(token);
            return true; 
        } catch (JwtException e) {
            return false;
        }
    }

    // helper methods
    // get sign key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
