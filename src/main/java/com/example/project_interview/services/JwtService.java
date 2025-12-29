package com.example.project_interview.services;

import com.example.project_interview.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    public boolean isValid(String token, UserDetails user){
        try {
            String email = extractEmail(token);
            return (email.equals(user.getUsername())) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error while validating JWT", e);
            return false;
        }

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token,Claims::getExpiration);
    }

    public String extractEmail(String token){
        return extractClaims(token,Claims::getSubject);
    }


    public  <T> T extractClaims(String token, Function<Claims,T>resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    private Claims extractAllClaims(String token){
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSigninKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Failed to parse/verify JWT: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while parsing JWT", e);
            throw new IllegalArgumentException("Invalid token");
        }
    }


    private final String SECRET_KEY = "30b5a319dfb88150d536b55b9a63a8ccc2d0ea700d538db866ba1538ec955545";
    public String generateToken(User user){
        try {
            String token = Jwts
                    .builder()
                    .subject(user.getEmail())
                    .claim("role", user.getRole())
                    .claim("username", user.getDbUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+24*60*60*1000))
                    .signWith(getSigninKey())
                    .compact();
            return token;
        } catch (Exception e) {
            log.error("Failed to generate JWT for user {}", user.getEmail(), e);
            throw new IllegalStateException("Unable to generate token");
        }
    }


    private SecretKey getSigninKey() {
        try {
            byte[] keyByte = Decoders.BASE64URL.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyByte);
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT secret key format", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while creating signing key", e);
            throw new IllegalStateException("Invalid signing key");
        }
    }
}
