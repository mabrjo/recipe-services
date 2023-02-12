package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.IngredientDTO;
import com.mcccodeschool.recipeservices.dto.NutrientEntityDTO;
import com.mcccodeschool.recipeservices.dto.Recipe2DTO;
import com.mcccodeschool.recipeservices.importer.IngredientInfo;
import com.mcccodeschool.recipeservices.importer.NutrientInformation;
import com.mcccodeschool.recipeservices.importer.Results;
import com.mcccodeschool.recipeservices.model.NutrientEntity;
import com.mcccodeschool.recipeservices.repository.NutrientEntityRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SpoonacularService {

    private static final Logger log = LoggerFactory.getLogger(SpoonacularService.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NutrientEntityRepository nutrientEntityRepository;
    @Autowired
    private RestSpoonacularService restSpoonacularService;

    public NutrientEntityDTO saveToDb(Results result, NutrientInformation nutrientInformation) {
        NutrientEntity nutrientEntity;
        nutrientEntity = modelMapper.map(nutrientInformation,NutrientEntity.class);
        nutrientEntity.setName(result.getName());
        nutrientEntity.setNutrients(nutrientInformation.getNutrition().getNutrients());
        nutrientEntity.setEstimatedCostValue(nutrientInformation.getEstimatedCost().getValue());
        nutrientEntity.setEstimatedCostUnit(nutrientInformation.getEstimatedCost().getUnit());
        nutrientEntity.setCaloricBreakdownPercentProtein(nutrientInformation.getNutrition().getCaloricBreakdown().getPercentProtein());
        nutrientEntity.setCaloricBreakdownPercentFat(nutrientInformation.getNutrition().getCaloricBreakdown().getPercentFat());
        nutrientEntity.setCaloricBreakdownPercentCarbs(nutrientInformation.getNutrition().getCaloricBreakdown().getPercentCarbs());
        nutrientEntity.setWeightPerServingAmount(nutrientInformation.getNutrition().getWeightPerServing().getAmount());
        nutrientEntity.setWeightPerServingUnit(nutrientInformation.getNutrition().getWeightPerServing().getUnit());

        log.info("Testing Services: "+nutrientEntity);
        NutrientEntity ne = nutrientEntityRepository.save(nutrientEntity);
        return modelMapper.map( ne ,NutrientEntityDTO.class);
    }

    public NutrientEntityDTO saveToDbV2(Results result, NutrientInformation nutrientInformation, Long recipeId) {
        NutrientEntity nutrientEntity;
        nutrientEntity = modelMapper.map(nutrientInformation,NutrientEntity.class);
        nutrientEntity.setName(result.getName());
        nutrientEntity.setRecipeId(recipeId);
        nutrientEntity.setNutrients(nutrientInformation.getNutrition().getNutrients());
        nutrientEntity.setEstimatedCostValue(nutrientInformation.getEstimatedCost().getValue());
        nutrientEntity.setEstimatedCostUnit(nutrientInformation.getEstimatedCost().getUnit());
        nutrientEntity.setCaloricBreakdownPercentProtein(nutrientInformation.getNutrition().getCaloricBreakdown().getPercentProtein());
        nutrientEntity.setCaloricBreakdownPercentFat(nutrientInformation.getNutrition().getCaloricBreakdown().getPercentFat());
        nutrientEntity.setCaloricBreakdownPercentCarbs(nutrientInformation.getNutrition().getCaloricBreakdown().getPercentCarbs());
        nutrientEntity.setWeightPerServingAmount(nutrientInformation.getNutrition().getWeightPerServing().getAmount());
        nutrientEntity.setWeightPerServingUnit(nutrientInformation.getNutrition().getWeightPerServing().getUnit());

        log.info("Testing Services: "+nutrientEntity);
        NutrientEntity ne = nutrientEntityRepository.save(nutrientEntity);
        return modelMapper.map( ne ,NutrientEntityDTO.class);
    }

    public NutrientEntityDTO addIngredient(String ingredientName){
        if(ingredientExists(ingredientName)){
            return callIngredientFromDb(ingredientName);
        }
        else{
            return fetchIngredient(ingredientName);
        }
    }

    public NutrientEntityDTO addIngredientV2(IngredientDTO ingredientDTO,Long recipeId){
        try {
            IngredientInfo ingredientInfo = restSpoonacularService.fetchIngredient(ingredientDTO.getContent());
            NutrientInformation nutrientInformation;
            for (Results result : ingredientInfo.getResults()) {
                if (result.getName().equalsIgnoreCase(ingredientDTO.getContent())) {
                    nutrientInformation = restSpoonacularService.fetchNutrientsV2(ingredientDTO, result);
                    return saveToDbV2(result, nutrientInformation, recipeId);
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public boolean ingredientExists(String ingredientName){
        Optional<NutrientEntity> option = nutrientEntityRepository.findByName(ingredientName.toLowerCase().trim());
        if(option.isPresent())
        {
            log.info("INGREDIENT ALREADY EXISTS");
            return true;
        }
        return false;
    }

    public NutrientEntityDTO callIngredientFromDb(String ingredientName) {
        Optional<NutrientEntity> optionalNutrientEntity = nutrientEntityRepository.findByName(ingredientName);
        if (optionalNutrientEntity.isPresent())
            return modelMapper.map(optionalNutrientEntity.get(), NutrientEntityDTO.class);
        return null;
    }

    private NutrientEntityDTO fetchIngredient(String ingredient){
        IngredientInfo ingredientInfo = restSpoonacularService.fetchIngredient(ingredient);
        log.info(ingredientInfo.toString());
        for (Results result : ingredientInfo.getResults()) {
            if (result.getName().equalsIgnoreCase(ingredient))
                return fetchNutrients(result);
        }
        return null;
    }

    private NutrientEntityDTO fetchNutrients(Results result){
        NutrientInformation nutrientInformation = restSpoonacularService.fetchNutrients(result);
        log.info(nutrientInformation.toString());
        return saveToDb(result,nutrientInformation);
    }
}
