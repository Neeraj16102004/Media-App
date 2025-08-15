package com.example.media_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
@Service
public class StreamTokenService {

    @Value("${stream.token.secret}")
    private String streamSecret;

    @Value("${stream.token.expiry-minutes:10}")
    private long streamExpiryMinutes;

    private Key streamKey;

    @PostConstruct
    public void init() {
        this.streamKey = Keys.hmacShaKeyFor(streamSecret.getBytes());
    }

    public String generateStreamToken(Long mediaId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject("stream")
                .claim("mediaId", mediaId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(streamExpiryMinutes * 60L)))
                .signWith(streamKey)
                .compact();
    }

    public Long validateStreamToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) streamKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("mediaId", Long.class);
        } catch (JwtException e) {
            return null; // Invalid token
        }
    }
}

