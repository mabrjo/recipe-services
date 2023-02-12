package com.mcccodeschool.recipeservices.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcccodeschool.recipeservices.dto.IngredientDTO;
import com.mcccodeschool.recipeservices.importer.IngredientInfo;
import com.mcccodeschool.recipeservices.importer.NutrientInformation;
import com.mcccodeschool.recipeservices.importer.Results;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
public class RestSpoonacularService {

    private static final Logger log = LoggerFactory.getLogger(RestSpoonacularService.class);

    private RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public IngredientInfo fetchIngredient(String ingredient) {
        IngredientInfo ingredientInfo = fetchIngredientFromDisk(ingredient);
        if (ingredientInfo != null)
            return ingredientInfo;
        RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());
        ingredientInfo = restTemplate.getForObject(
                String.format("https://api.spoonacular.com/food/ingredients/search?apiKey=%s&query=%s&metaInformation=true&sort=calories&sortDirection=desc",
                        System.getenv("SPOONACULAR_API_Key"),ingredient),
                IngredientInfo.class);
        saveIngredientToDisk(ingredient,ingredientInfo);
        return ingredientInfo;
    }

    private void saveIngredientToDisk(String ingredient, IngredientInfo ingredientInfo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(String.format("src/main/resources/API_JSON/IngredientInfo_%s.json",ingredient)), ingredientInfo);
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }

    private IngredientInfo fetchIngredientFromDisk(String ingredient) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            IngredientInfo ingredientInfo = objectMapper.readValue(new File(String.format("src/main/resources/API_JSON/IngredientInfo_%s.json",ingredient)),IngredientInfo.class);
            return ingredientInfo;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }

    public NutrientInformation fetchNutrients(Results result) {
        NutrientInformation nutrientInformation = fetchNutrientsFromDisk(result);
        if (nutrientInformation != null)
            return nutrientInformation;
        RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());
        nutrientInformation = restTemplate.getForObject(
                String.format("https://api.spoonacular.com/food/ingredients/%d/information?apiKey=%s&amount=1",
                        result.getId(), System.getenv("SPOONACULAR_API_Key")),
                NutrientInformation.class);
        saveNutrientsToDisk(nutrientInformation);
        return nutrientInformation;
    }

    public NutrientInformation fetchNutrientsV2(IngredientDTO ingredientDTO,Results result) {
        NutrientInformation nutrientInformation = fetchNutrientsFromDisk(result);
        if (nutrientInformation != null)
            return nutrientInformation;
        RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());
        nutrientInformation = restTemplate.getForObject(
                String.format("https://api.spoonacular.com/food/ingredients/%d/information?apiKey=%s&amount=%d&unit=%s",
                        result.getId(),
                        System.getenv("SPOONACULAR_API_Key"),
                        ingredientDTO.getQuantity(),
                        ingredientDTO.getMeasure()),
                NutrientInformation.class);
        saveNutrientsToDisk(nutrientInformation);
        return nutrientInformation;
    }

    private void saveNutrientsToDisk(NutrientInformation nutrientInformation){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(String.format("src/main/resources/API_JSON/IngredientInfo_%d.json",
                    nutrientInformation.getId())), nutrientInformation);
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }

    private NutrientInformation fetchNutrientsFromDisk(Results result) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            NutrientInformation nutrientInformation = objectMapper.readValue(new File(String.format("src/main/resources/API_JSON/IngredientInfo_%d.json",
                    result.getId())),NutrientInformation.class);
            return nutrientInformation;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }

}
