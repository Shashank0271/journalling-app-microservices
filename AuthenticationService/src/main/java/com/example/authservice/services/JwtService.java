package com.example.authservice.services;

import com.example.authservice.dtos.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateAccessToken(UserDTO user) {
        return Jwts.builder().
                subject(user.getId()).
                claim("email", user.getEmail()).
                issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis() + 24L * 60L * 60L * 1000L)).
                signWith(getSecretKey()).
                compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
