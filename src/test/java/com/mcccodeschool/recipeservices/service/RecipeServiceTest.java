package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.RecipeDTO;
import com.mcccodeschool.recipeservices.model.Recipe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeServiceTest {
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Test
//    void convertRecipeEntityToDTO(){
//        RecipeDTO recipeDTO = new RecipeDTO(null,"ingredient","instruction","name","URI",1,1,"user");
//        Recipe recipe = new Recipe("ingredient","instruction","name","URI",1,1,"user");
//        RecipeDTO converted = modelMapper.map(recipe,RecipeDTO.class);
//        assertEquals(converted.getIngredients(),recipe.getIngredients());
//    }
}
