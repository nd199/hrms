package com.naren.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long accessExpiresIn;
    private final long refreshExpiresIn;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiry-seconds}") long accessExpiresIn,
            @Value("${jwt.refresh-expiry-seconds}") long refreshExpiresIn
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.accessExpiresIn = accessExpiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String generateAccessToken(String username, List<String> roles) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(accessExpiresIn)))
                .sign(algorithm);
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(refreshExpiresIn)))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        }
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public LocalDateTime getExpiresAt(String token) {
        Instant expiresAt = validateToken(token).getExpiresAt().toInstant();
        return LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault());
    }

    public long getAccessExpiresInSeconds() {
        return accessExpiresIn;
    }

    public long getRefreshExpiresInSeconds() {
        return refreshExpiresIn;
    }
}
