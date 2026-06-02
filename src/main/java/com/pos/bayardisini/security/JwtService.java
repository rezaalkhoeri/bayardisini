package com.pos.bayardisini.security;

import com.pos.bayardisini.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        public String generateToken(User user) {

                SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

                return Jwts.builder()
                                .subject(user.getId().toString())
                                .claim("email", user.getEmail())
                                .claim("tenantId", user.getTenantId())
                                .issuedAt(new Date())
                                .expiration(
                                                new Date(
                                                                System.currentTimeMillis()
                                                                                + expiration))
                                .signWith(key)
                                .compact();
        }

        public String extractUsername(String token) {
                return Jwts.parser()
                                .verifyWith(getSignInKey())
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .get("email", String.class);
        }

        private SecretKey getSignInKey() {
                return Keys.hmacShaKeyFor(
                                secret.getBytes());
        }

        public boolean isTokenValid(String token) {
                try {
                        Jwts.parser()
                                        .verifyWith(getSignInKey())
                                        .build()
                                        .parseSignedClaims(token);

                        return true;
                } catch (Exception e) {
                        return false;
                }
        }
}