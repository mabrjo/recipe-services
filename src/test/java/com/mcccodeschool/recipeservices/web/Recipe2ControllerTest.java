package com.mcccodeschool.recipeservices.web;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcccodeschool.recipeservices.config.AppProperties;
import com.mcccodeschool.recipeservices.dto.*;
import com.mcccodeschool.recipeservices.model.User2;
import com.mcccodeschool.recipeservices.repository.User2Repository;
import com.mcccodeschool.recipeservices.security.UserPrincipal;
import com.mcccodeschool.recipeservices.service.CustomOAuth2UserService;
import com.mcccodeschool.recipeservices.service.Recipe2Service;
import com.mcccodeschool.recipeservices.service.User2Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hamcrest.Condition;
import org.hamcrest.Matchers;
import org.hibernate.annotations.DiscriminatorOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class Recipe2ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppProperties appProperties;

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private Recipe2Service recipe2Service;

    Random rand = new Random();

    @BeforeEach
    void setSecurityContext() {
        String token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
        headers.set("Authorization", "Bearer " + token);
    }

    User2DTO user2DTO() {
        User2DTO user2DTO = new User2DTO();
        user2DTO.setId(123L);
        user2DTO.setUsername("test");
        user2DTO.setEnabled(true);
        user2DTO.setEmail("test" + String.valueOf(rand.nextInt(999)) + "@test.com");
        user2DTO.setProvider("testProvider");
        user2DTO.setProviderId("123");
        return user2DTO;
    }

    @Test
    @DisplayName("Gets all recipes from the database. No ordering, filtering, or pagination")
    void getsAllRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe")
                .headers(headers)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Gets all recipes with 1 category as a query parameter")
    void getsRecipesByOneCategory() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe?category=2")
                .headers(headers)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Gets all recipes with *multiple categories as a query parameter")
    void getsRecipesByMultipleCategory() throws Exception{
        // Correctly only returns a recipe once
        mockMvc.perform(get("/api/v2/recipe?category=10,11,12")
                .headers(headers)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(2));
        mockMvc.perform(get("/api/v2/recipe?category=4,11,16")
                .headers(headers)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Handles a bad 'category' query param by returning empty list")
    void handlesBadParam() throws Exception {
        mockMvc.perform(get("/api/v2/recipe?category=asdf")
                .headers(headers)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        mockMvc.perform(get("/api/v2/recipe?category=")
                .headers(headers)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Unable to get all recipes from the database because not authorized")
    void doesNotGetAllRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns a specific recipe object with a given Recipe ID")
    void testGetRecipeByIdFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe/1", 1)
                .headers(headers)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Returns recipe not found when ID provided does not exist in the Database")
    void testGetRecipeByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe/4444", 1)
                .headers(headers)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns unauthorized because unauthenticated")
    void testGetRecipeByIdNotAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe/1", 1)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns success message on recipe submit and returns true if ID exists in DB")
    void testPostRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("http://localhost:8080/api/v2/recipe")
                .headers(headers)
                .content(asJsonString(new Recipe2DTO(
                        null,
                        "title",
                        Set.of(new CategoryDTO(
                                null,
                                "cat name"
                        )),
                        Set.of(new IngredientDTO(
                                null,
                                "ingredient content",
                                20L,
                                "cups"
                        )),
                        Set.of(new InstructionDTO(
                                null,
                                "Instruction content",
                                (long) 12
                        )),
                        "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg?quality=90&resize=700%2C636",
                        (long) 2,
                        (long) 3,
                        null,
                        user2DTO()
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }


    @Test
    @DisplayName("Returns unauthorized for an unauthorized user posting recipe")
    void testPostRecipeNotAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("http://localhost:8080/api/v2/recipe")
                .content(asJsonString(new Recipe2DTO(
                        null,
                        "title"
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Returns accepted status on recipe put and confirms change to recipe title")
    void testPutRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("http://localhost:8080/api/v2/recipe")
                .headers(headers)
                .content(asJsonString(new Recipe2DTO(
                        1L,
                        "Mac & Cheese"
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Mac & Cheese"));
    }

    @Test
    @DisplayName("Returns accepted status on recipe put and confirms change to recipe title")
    void testFullPutRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("http://localhost:8080/api/v2/recipe")
                .headers(headers)
                .content(asJsonString(new Recipe2DTO(
                        1L,
                        "Mac & Cheese",
                        Set.of(new CategoryDTO(null, "Breakfast"), new CategoryDTO(null, "Gluten Free")),
                        Set.of(new IngredientDTO(2L, "eggs", 2L, "grams"), new IngredientDTO(1L, "flourXXXXXXXX", 12L, "cups"), new IngredientDTO("taters", 33L, "tons")),
                        Set.of(new InstructionDTO(1L, "Boil Mac", 1L), new InstructionDTO(2L, "Add Cheese", 2L)),
                        null,
                        5L,
                        5L,
                        null,
                        null
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                // Based on this random google result:
                // https://medium.com/@valeryyakovlev/convenient-way-to-test-that-list-contains-string-items-in-any-order-7209f4e1fdd1
                .andExpect(jsonPath("$.ingredients[*].content").value(containsInAnyOrder("taters"
                        , "eggs"
                        , "flourXXXXXXXX"
                )))
                ;
    }

    @Test
    @DisplayName("Returns not authorized for unauthorized user recipe put")
    void testPutRecipeNotAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("http://localhost:8080/api/v2/recipe")
                .content(asJsonString(new Recipe2DTO(
                        1L,
                        "Mac & Cheese"
                )))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Return status of OK on get userSearch recipes")
    void testGetRecipesBySearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe/search")
                .param("userSearch", "lot")
                .headers(headers)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Return status of OK on get userSearch recipes")
    void testGetRecipesByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v2/recipe")
                .param("user", "1")
                .headers(headers)
        )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns ADMIN username in order to append to a Recipe2 object being sent to the frontend")
    void testGetUserData() throws Exception {
        Long recipeId = 1L;
        User2DTO user2;
        user2 = recipe2Service.getUserData(recipeId);
        assertEquals("ADMIN", user2.getUsername());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
