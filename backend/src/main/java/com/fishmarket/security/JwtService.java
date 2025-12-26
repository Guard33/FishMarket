package com.fishmarket.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
  private final Key signingKey;
  private final String issuer;
  private final long ttlMinutes;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.issuer}") String issuer,
      @Value("${app.jwt.ttl-minutes}") long ttlMinutes) {
    this.signingKey = buildKey(secret);
    this.issuer = issuer;
    this.ttlMinutes = ttlMinutes;
  }

  public String issueToken(String subject) {
    Instant now = Instant.now();
    Instant expiresAt = now.plusSeconds(ttlMinutes * 60);
    return Jwts.builder()
        .setSubject(subject)
        .setIssuer(issuer)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiresAt))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
  }

  private Key buildKey(String secret) {
    if (secret == null || secret.isBlank()) {
      throw new IllegalArgumentException("JWT secret is required.");
    }
    byte[] raw;
    if (secret.startsWith("base64:")) {
      raw = Decoders.BASE64.decode(secret.substring("base64:".length()));
    } else {
      raw = secret.getBytes(StandardCharsets.UTF_8);
    }
    if (raw.length < 32) {
      throw new IllegalArgumentException("JWT secret must be at least 32 bytes.");
    }
    return Keys.hmacShaKeyFor(raw);
  }
}
