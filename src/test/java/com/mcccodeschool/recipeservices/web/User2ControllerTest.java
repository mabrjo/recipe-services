package com.mcccodeschool.recipeservices.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcccodeschool.recipeservices.config.AppProperties;
import com.mcccodeschool.recipeservices.dto.UserInfoDTO;
import com.mcccodeschool.recipeservices.security.User2Principal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class User2ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppProperties appProperties;

    HttpHeaders headers(){
        HttpHeaders headers = new HttpHeaders();
        String token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    List<GrantedAuthority> authorities = Collections.
            singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    User2Principal user2Principal = new User2Principal(1L, "test@email.com", "googlehub", authorities);
    OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(user2Principal, user2Principal.getAuthorities(), user2Principal.getProvider());


    @Test
    @DisplayName("user endpoint returns 401 because no USER set in securitycontext")
    void doesNotGetUserInformation() throws Exception {
        mockMvc.perform(
                get("/api/v2/user", 1)).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("retrieves all users when no query param passed url")
    void getsAllUsers() throws Exception {
        mockMvc.perform(
                get("/api/v2/users").headers(headers())).andExpect(status().isOk());
    }

    @Test
    @DisplayName("retrieves specific user when 'id' query param passed in the url")
    void getsUserById() throws Exception {
        mockMvc.perform(
                get("/api/v2/users?id=1")
                .headers(headers()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Gets currently authenticated User")
    void getsUserInformation() throws Exception {
        mockMvc.perform(
                get("/api/v2/user", 1)
                    .headers(headers()))
//                    .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Gets all usernames")
    void getsUsernames() throws Exception{
        mockMvc.perform(
                get("/api/v2/usernames")
                .headers(headers()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Successfully updates user")
    void updatesUser() throws Exception{
        String x = randomStringGenerator(10);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setDisplayName(x);
        userInfoDTO.setId(1L);
        mockMvc.perform(
                put("/api/v2/user")
                .content(new ObjectMapper().writeValueAsString(userInfoDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
//                .with(authentication(authentication)))
                .headers(headers()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.displayName").value(x));
    }

    @Test
    @DisplayName("Fails to update user because unauthenticated")
    void doesNotUpdateUnauthenticatedUser() throws Exception{
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        mockMvc.perform(
                put("/api/v2/user")
                .content(new ObjectMapper().writeValueAsString(userInfoDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Fails to update another user other than the one currently signed in")
    void doesNotUpdateDifferentUser() throws Exception{
        String x = randomStringGenerator(10);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setDisplayName(x);
        userInfoDTO.setId(2L);
        mockMvc.perform(
                put("/api/v2/user")
                        .headers(headers())
                        .content(new ObjectMapper().writeValueAsString(userInfoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Bad request to update user")
    void badRequestToUpdateUser() throws Exception{
        mockMvc.perform(
                put("/api/v2/user")
                        .headers(headers()))
                .andExpect(status().isBadRequest());
    }



    String randomStringGenerator(int stringLength){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}

