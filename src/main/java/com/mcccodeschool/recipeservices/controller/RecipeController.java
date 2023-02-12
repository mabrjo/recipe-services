package com.mcccodeschool.recipeservices.controller;

import com.mcccodeschool.recipeservices.dto.RecipeDTO;
import com.mcccodeschool.recipeservices.service.RecipeService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    /**
     * Get Mapping to request All Recipes
     */
    @GetMapping
    public List<RecipeDTO> getRecipes() {
        return recipeService.findAll();
    }

    /**
     * Get Mapping to request a single Recipe by ID
     */
    @GetMapping("/{id}")
    public RecipeDTO getRecipeById(@PathVariable Long id) {
        return recipeService.findById(id);
    }

    /**
     * Post Mapping to add a recipe to DB
     */
    @PostMapping
    public RecipeDTO postRecipe(@Validated @RequestBody @NotNull RecipeDTO recipeDTO) {
        return recipeService.save(recipeDTO);
    }

    /**
     * Delete Mapping to remove a Recipe
     */
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteById(id);
    }
}
