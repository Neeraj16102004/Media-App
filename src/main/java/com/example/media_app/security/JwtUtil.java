package com.example.media_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key;
    private final long jwtExpirationMs;
    private final long streamExpirationSec;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long jwtExpirationMs,
            @Value("${app.jwt.stream.expiration}") long streamExpirationSec
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
        this.streamExpirationSec = streamExpirationSec;
    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String generateStreamToken(Long mediaId) {
        return Jwts.builder()
                .subject("stream")
                .claim("mediaId", mediaId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + streamExpirationSec * 1000))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public Long getMediaIdFromStreamToken(String token) {
        Object mediaId = Jwts.parser().verifyWith((SecretKey) key).build()
                .parseSignedClaims(token).getPayload().get("mediaId");
        return mediaId != null ? Long.valueOf(mediaId.toString()) : null;
    }
}


