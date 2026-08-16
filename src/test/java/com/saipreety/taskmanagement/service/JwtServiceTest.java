package com.saipreety.taskmanagement.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;
    private String secretKey;

    @BeforeEach
    void setUp() {

        SecretKey key = Keys.secretKeyFor(
                io.jsonwebtoken.SignatureAlgorithm.HS256
        );

        secretKey = Encoders.BASE64.encode(key.getEncoded());

        jwtService = new JwtService(secretKey);

        userDetails = User.builder()
                .username("test@gmail.com")
                .password("password123")
                .roles("USER")
                .build();
    }

    @Test
    void generateToken_ShouldGenerateToken() {

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnUsername() {

        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("test@gmail.com", username);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {

        String token = jwtService.generateToken(userDetails);

        boolean result = jwtService.isTokenValid(token, userDetails);

        assertTrue(result);
    }

    @Test
    void isTokenValid_ShouldReturnFalseForDifferentUser() {

        String token = jwtService.generateToken(userDetails);

        UserDetails anotherUser = User.builder()
                .username("another@gmail.com")
                .password("password123")
                .roles("USER")
                .build();

        boolean result = jwtService.isTokenValid(token, anotherUser);

        assertFalse(result);
    }
}