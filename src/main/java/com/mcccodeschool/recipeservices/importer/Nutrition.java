package com.mcccodeschool.recipeservices.importer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mcccodeschool.recipeservices.model.Nutrient;
import com.mcccodeschool.recipeservices.model.NutritionProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nutrition {
    private List<Nutrient> nutrients;
    private List<NutritionProperties> properties;
    private CaloricBreakdown caloricBreakdown;
    private WeightPerServing weightPerServing;
}
