package com.mcccodeschool.recipeservices.web;


import com.mcccodeschool.recipeservices.config.AppProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppProperties appProperties;

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setSecurityContext() {
        String token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
        headers.set("Authorization", "Bearer " + token);
    }

    @Test
    @DisplayName("Gets all ingredients from the database. No ordering, filtering, or pagination")
    void getsAllIngredientsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/ingredient")
                .headers(headers)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("grabs ingredient by id")
    void getIngredientByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/ingredient/1")
                .headers(headers)
        ).andExpect(status().isOk());
    }

}
