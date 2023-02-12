package com.mcccodeschool.recipeservices.controller;

import com.mcccodeschool.recipeservices.dto.IngredientDTO;
import com.mcccodeschool.recipeservices.model.Ingredient;
import com.mcccodeschool.recipeservices.service.CategoryService;
import com.mcccodeschool.recipeservices.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api/v2/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllIngredients() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getAllIngredients());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Long id) {
        IngredientDTO ingredientDTO = ingredientService.getIngredientById(id);
        if (ingredientDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not found with id: " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ingredientDTO);
    }
}
