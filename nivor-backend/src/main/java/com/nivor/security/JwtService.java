package com.nivor.security;

import com.nivor.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final Key key;
    private final long expirationMs;
    public JwtService(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long expirationMs) {
        if (secret == null || secret.isBlank()) throw new IllegalStateException("JWT_SECRET must be configured");
        if (expirationMs <= 0) throw new IllegalStateException("JWT_EXPIRATION_MS must be greater than zero");
        byte[] bytes;
        try { bytes = Decoders.BASE64.decode(secret); } catch (RuntimeException ignored) { bytes = secret.getBytes(StandardCharsets.UTF_8); }
        if (bytes.length < 32) throw new IllegalStateException("JWT_SECRET must contain at least 32 bytes");
        this.key = Keys.hmacShaKeyFor(bytes); this.expirationMs = expirationMs;
    }
    public String createToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getEmail()).claim("uid", user.getId()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expirationMs))).signWith(key).compact();
    }
    public String extractEmail(String token) { return claims(token).getSubject(); }
    public boolean isValid(String token, String email) { Claims claims = claims(token); return email.equalsIgnoreCase(claims.getSubject()) && claims.getExpiration().after(new Date()); }
    private Claims claims(String token) { return Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token).getPayload(); }
}
