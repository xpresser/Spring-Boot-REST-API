package com.metodi.projectapp.services.impl;

import com.metodi.projectapp.entities.User;
import com.metodi.projectapp.security.CustomUserPrincipal;
import com.metodi.projectapp.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTTokenService implements TokenService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Override
    public String generateToken(User user) {

        Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS);
        Date date = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        String token = Jwts.builder()
                .claim("id", user.getId())
                .claim("sub", user.getUsername())
                .claim("admin", user.isAdmin())
                .setExpiration(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + token;
    }

    @Override
    public CustomUserPrincipal parseToken(String token) {

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET.getBytes())
                .build()
                .parseClaimsJws(token);

        Claims body = claimsJws.getBody();

        String username = body.getSubject();

        long id = body.get("id", Long.class);

        boolean isAdmin = body.get("admin", Boolean.class);

        return new CustomUserPrincipal(id, username, isAdmin);
    }
}
