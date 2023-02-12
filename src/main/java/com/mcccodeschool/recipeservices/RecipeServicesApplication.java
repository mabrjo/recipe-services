package com.mcccodeschool.recipeservices;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcccodeschool.recipeservices.config.AppProperties;
import com.mcccodeschool.recipeservices.dto.*;
import com.mcccodeschool.recipeservices.service.CollectionService;
import com.mcccodeschool.recipeservices.service.Recipe2Service;
import com.mcccodeschool.recipeservices.service.SpoonacularService;
import com.mcccodeschool.recipeservices.service.User2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;


@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class RecipeServicesApplication implements CommandLineRunner  {

	@Autowired
	private Recipe2Service recipe2Service;
	@Autowired
	private User2Service user2Service;
	@Autowired
	private CollectionService collectionService;
	@Autowired
	private SpoonacularService spoonacularService;

	final String jsonFile = "src/main/resources/recipe_sample_data.json";
	final String jsonFile2 = "src/main/resources/class_recipes.json";

	private static final Logger log = LoggerFactory.getLogger(RecipeServicesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RecipeServicesApplication.class, args);
	}

	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {
		if (recipe2Service.findAll().isEmpty()) {
			importRecipes();
			Recipe2DTO r2DTO = recipe2Service.findAll().get(0);
			User2DTO user2DTO = new User2DTO();
			user2DTO.setProvider("test");
			user2DTO.setProviderId("123");
			user2DTO.setEmail("admin@admin.com");
			user2DTO.setUsername("ADMIN");
			user2DTO.setId(1L);
			user2DTO.setEnabled(true);
			User2DTO savedUser2 = user2Service.saveUser(user2DTO);
			user2Service.addRecipe(savedUser2.getId(), r2DTO.getId());

			/**
			 * Creating Test Recipe with known, built ingredients / instructions
			 */
			Set<InstructionDTO> instructionDTOList = Set.of(
					new InstructionDTO("Add Carrots to bowel", Long.valueOf(1)),
					new InstructionDTO("Add Lettuce to bowel", Long.valueOf(2)),
					new InstructionDTO("Add Tomatoes to bowel", Long.valueOf(3)),
					new InstructionDTO("Stir vegetables until salad like", Long.valueOf(4)));

			Set<IngredientDTO> ingredientDTOList = Set.of(
					new IngredientDTO("Carrots", Long.valueOf(6), "piece"),
					new IngredientDTO("Lettuce", Long.valueOf(1), "serving"),
					new IngredientDTO("Tomatoes", Long.valueOf(5), "fruit"));
			Recipe2DTO newRecipe = new Recipe2DTO();
			newRecipe.setTitle("Cesar Salad");
			newRecipe.setInstructions(instructionDTOList);
			newRecipe.setIngredients(ingredientDTOList);
			newRecipe.setPrepTime(Long.valueOf(6));
			newRecipe.setCookTime(Long.valueOf(0));
			newRecipe = recipe2Service.addRecipe(newRecipe);
			/**
			 * Added a method in Recipe2Service to add nutrient entities of saved recipes given recipe ID
			 */
			recipe2Service.addNutrientEntitiesByRecipe(newRecipe.getId());
			recipe2Service.addNutrientEntitiesByRecipe(1L);
			recipe2Service.addNutrientEntitiesByRecipe(2L);
			recipe2Service.addNutrientEntitiesByRecipe(3L);
		}
	}

	public void importRecipes() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Recipe2DTO[] recipe2DTOs = objectMapper.readValue(new File(jsonFile2), Recipe2DTO[].class);
		for (Recipe2DTO recipe2DO : recipe2DTOs){
			recipe2Service.addRecipe(recipe2DO);
		}
		recipe2DTOs = objectMapper.readValue(new File(jsonFile), Recipe2DTO[].class);
		for (Recipe2DTO recipe2DO : recipe2DTOs){
			recipe2Service.addRecipe(recipe2DO);
		}

	}
}
