package com.mcccodeschool.recipeservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcccodeschool.recipeservices.dto.Recipe2DTO;
import com.mcccodeschool.recipeservices.dto.*;
import com.mcccodeschool.recipeservices.security.User2Principal;
import com.mcccodeschool.recipeservices.service.Recipe2Service;
import com.mcccodeschool.recipeservices.service.User2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;
import java.io.*;
import java.util.Set;
import org.springframework.util.ResourceUtils;

import javax.transaction.Transactional;

@Controller
@RestController
@RequestMapping("/api/v2/recipe")
@Transactional
public class Recipe2Controller {

    private final Recipe2Service recipe2Service;

    @Autowired
    User2Service user2Service;

    public Recipe2Controller(Recipe2Service recipe2Service) {
        this.recipe2Service = recipe2Service;
    }

    @GetMapping()
    public ResponseEntity<?> getAllRecipes(@RequestParam Map<String, String> params){
        //TODO: take in parameters for sort options

        if (params.containsKey("q")){
            return ResponseEntity.status(HttpStatus.OK).body(recipe2Service.findRecipesByUserSearch(params.get("q")));
        }
        if (params.containsKey("category")){
            return ResponseEntity.status(HttpStatus.OK).body(recipe2Service.findAllByCategory(params.get("category")));
        }
        if (params.containsKey("ingredient")){
            return ResponseEntity.status(HttpStatus.OK).body(recipe2Service.findAllByIngredient(params.get("ingredient")));
        }
        if (params.containsKey("user")) {
            return ResponseEntity.status(HttpStatus.OK).body(recipe2Service.findRecipesByUserId(params.get("user")));
        }


        return ResponseEntity.status(HttpStatus.OK).body(recipe2Service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        Recipe2DTO recipe2DTO = recipe2Service.getRecipeById(id);
        if (recipe2DTO == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe not found with id: " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(recipe2DTO);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getRecipesBySearch(@RequestParam( value="userSearch",required = true) String userSearch) {
        try {
            Set<Recipe2DTO> recipeDTOS = recipe2Service.findRecipesByUserSearch(userSearch);
            return ResponseEntity.status(HttpStatus.OK).body(recipeDTOS);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : " + ex.getMessage());
        }
    }


    @PostMapping()
    public ResponseEntity<?> newRecipe(@Validated @RequestBody Recipe2DTO recipe2DTO) {
        try {
            User2Principal principal = (User2Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Recipe2DTO returnedRecipe2DTO = recipe2Service.createRecipe(recipe2DTO);
            User2DTO returnedUser2DTO = user2Service.addRecipe(principal.getId(), returnedRecipe2DTO.getId());

            ObjectMapper objectMapper = new ObjectMapper();
            String str = objectMapper.writeValueAsString(returnedRecipe2DTO);
            System.out.println(str);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnedRecipe2DTO);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : " + ex.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateRecipe(@Validated @RequestBody Recipe2DTO recipe2DTO) {
        try {
        Recipe2DTO returnedRecipe2DTO = recipe2Service.updateRecipe(recipe2DTO);
            ObjectMapper objectMapper = new ObjectMapper();
            String str = objectMapper.writeValueAsString(returnedRecipe2DTO);
            System.out.println(str);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(returnedRecipe2DTO);
        } catch (Exception ex) {
            Map<String, String> body = new HashMap<>();
            body.put("error", "Failed to update Recipe ID: " + recipe2DTO.getId());
            body.put("exception", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @GetMapping("nutrients/{id}")
    public List<NutrientEntityDTO> getNutrientInfoFromRecipe(@PathVariable Long id){
        return recipe2Service.getNutrientsByRecipe(id);
    }


    @GetMapping("/deploy")
    public String getDeployInfo() {
	StringBuilder result= new StringBuilder("DEFAULT STUFF\n");
	try {
	  String fn = "deploy.txt";
	  InputStream is = (new ClassPathResource(fn)).getInputStream();

	  String line;
	  LineNumberReader in = new LineNumberReader(
                          new InputStreamReader(is));
	  while (null != (line = in.readLine())) {
            result.append(line).append("\n"); // slow  ya know
	  }
	  in.close();
	} catch (Exception bland) {
	  bland.printStackTrace();
	}
	return String.format("<pre>%s</pre>", result.toString());
    }
}
