package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.RecipeDTO;
import com.mcccodeschool.recipeservices.model.Recipe;
import com.mcccodeschool.recipeservices.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;
    ModelMapper modelMapper = new ModelMapper();

    public List<RecipeDTO> findAll() {
        List<Recipe> allRecipes = recipeRepository.findAll();
        List<RecipeDTO> allRecipesDTO = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            allRecipesDTO.add(modelMapper.map(recipe, RecipeDTO.class));
        }
        return allRecipesDTO;
    }

    public RecipeDTO findById(Long id) {
        Optional<Recipe> recipeDB = recipeRepository.findById(id);
        if (recipeDB.isPresent()) {
            Recipe recipe = recipeDB.get();
            RecipeDTO recipeDTO = modelMapper.map(recipe, RecipeDTO.class);
            return recipeDTO;
        }
        return null;
    }

    public RecipeDTO save(RecipeDTO recipeDTO) {
        Recipe newRecipe = modelMapper.map(recipeDTO, Recipe.class);
        Recipe savedRecipe = recipeRepository.save(newRecipe);
        return modelMapper.map(savedRecipe, RecipeDTO.class);
    }

    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
