package com.mcccodeschool.recipeservices.security;

import com.mcccodeschool.recipeservices.config.AppProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.security.SignatureException;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TokenProviderTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private TokenProvider tokenProvider;

    String token;
//    eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjE4NDQxMzE5fQ.70S1Y-fPYQu0dXr7iQugBCfffO9IYE9CzHCkmnNMG1qMs2vYLqCXqRjr6nCZwxOVRKaPRif1DR1CW8KEN6FMEA

    @BeforeEach
    void generateToken(){
        token = Jwts.builder()
            .setSubject("1")
            .setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
            .compact();
    }


    @Test
    @DisplayName("Extracts token subject")
    void getsUserIdFromToken(){
        Assertions.assertEquals(tokenProvider.getUserIdFromToken(token).longValue(), 1L);
    }

    @Test
    @DisplayName("invalid token signature")
    void invalidTokenSignature(){
        token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "fakeKey")
                .compact();
        Assertions.assertFalse(tokenProvider.validateToken(token));
    }
    @Test
    @DisplayName("invalid token")
    void invalidToken(){
        Assertions.assertFalse(tokenProvider.validateToken("faketoken"));
    }
    @Test
    @DisplayName("expired token")
    void expiredToken(){
        token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
        Assertions.assertFalse(tokenProvider.validateToken(token));
    }

}
