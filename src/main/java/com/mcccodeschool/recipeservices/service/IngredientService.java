package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.CategoryDTO;
import com.mcccodeschool.recipeservices.dto.IngredientDTO;
import com.mcccodeschool.recipeservices.model.Category;
import com.mcccodeschool.recipeservices.model.Ingredient;
import com.mcccodeschool.recipeservices.repository.CategoryRepository;
import com.mcccodeschool.recipeservices.repository.IngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    public IngredientService(IngredientRepository ingredientRepository, ModelMapper modelMapper) {
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = modelMapper;
    }


    public List<IngredientDTO> getAllIngredients() {
        List<IngredientDTO> allIngredientsDTO = new ArrayList<>();
        ingredientRepository.findAll().stream().forEach((r) -> allIngredientsDTO.add(
                modelMapper.map(r, IngredientDTO.class)
        ));
        return allIngredientsDTO;
    }

    public IngredientDTO getIngredientById(Long id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isPresent()){
            IngredientDTO ingredientDTO = new IngredientDTO(id, ingredient.get().getContent(), ingredient.get().getQuantity(), ingredient.get().getMeasure());
            return ingredientDTO;
        } else {
            return null;
        }
    }

}
