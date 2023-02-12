package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.*;
import com.mcccodeschool.recipeservices.model.*;
import com.mcccodeschool.recipeservices.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Recipe2Service {

    private final Recipe2Repository recipe2Repository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final InstructionRepository instructionRepository;
    private final RecipeUpdateNoteRepository recipeUpdateNoteRepository;
    private final SpoonacularService spoonacularService;
    private final NutrientEntityRepository nutrientEntityRepository;
    private final ModelMapper modelMapper;
    private final User2Repository user2Repository;

    private static final Logger log = LoggerFactory.getLogger(Recipe2Service.class);


    public Recipe2Service(SpoonacularService spoonacularService, ModelMapper modelMapper,
                          Recipe2Repository recipe2Repository, CategoryRepository categoryRepository,
                          IngredientRepository ingredientRepository, InstructionRepository instructionRepository,
                          RecipeUpdateNoteRepository recipeUpdateNoteRepository,
                          NutrientEntityRepository nutrientEntityRepository, User2Repository user2Repository) {
        this.modelMapper = modelMapper;
        this.recipe2Repository = recipe2Repository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.instructionRepository = instructionRepository;
        this.recipeUpdateNoteRepository = recipeUpdateNoteRepository;
        this.spoonacularService = spoonacularService;
        this.nutrientEntityRepository = nutrientEntityRepository;
        this.user2Repository = user2Repository;
    }

    public List<Recipe2DTO> findAll() {
        List<Recipe2> all = recipe2Repository.findAll();
        List<Recipe2DTO> allDTO = new ArrayList<>();
        for (Recipe2 r : all) {
            Recipe2DTO rDTO = modelMapper.map(r, Recipe2DTO.class);
            allDTO.add(rDTO);
        }
        return allDTO;
    }

    public User2DTO getUserData(Long recipeId) {
        User2 user = user2Repository.findByRecipe2Id(recipeId);
        if(user == null) {
            Optional<User2> user2Id1 = user2Repository.findById(1L);
            return modelMapper.map(user2Id1, User2DTO.class);
        } else {
            return modelMapper.map(user, User2DTO.class);
        }

    }

    public List<Recipe2DTO> findAllByCategory(String categories){
        List<Recipe2> recipe2s = new ArrayList<>();
        if(!categories.contains(",")){
            try {
                recipe2s = recipe2Repository.findAllByCategory(Long.parseLong(categories));
            }
            catch(Exception ex){
                return List.of();
            }
        } else {
            String[] categoryIds = categories.split(",");
            for(String s : categoryIds){
                try{
                    recipe2s.addAll(recipe2Repository.findAllByCategory(Long.parseLong(s)));
                } catch(Exception ex){
                    return List.of();
                }
            }
        }
        List<Recipe2DTO> recipe2DTOS = new ArrayList<>();
        assert recipe2s != null;
        recipe2s.forEach(r -> recipe2DTOS.add(modelMapper.map(r, Recipe2DTO.class)));
        return new ArrayList<>(new HashSet<>(recipe2DTOS));
    }

    public List<Recipe2DTO> findAllByIngredient(String ingredients) {
        List<Recipe2> recipe2s = new ArrayList<>();
        if(!ingredients.contains(",")) {
            recipe2s = recipe2Repository.findAllByIngredient(Long.parseLong(ingredients));
        } else {
            String[] ingredientIds = ingredients.split(",");
            for(String s : ingredientIds) {
                try {
                    recipe2s.addAll(recipe2Repository.findAllByIngredient(Long.parseLong(s)));
                } catch(Exception ex) {
                    return null;
                }
            }
        }
        List<Recipe2DTO> recipe2DTOS = new ArrayList<>();
        assert recipe2s != null;
        recipe2s.forEach(r -> recipe2DTOS.add(modelMapper.map(r, Recipe2DTO.class)));
        return new ArrayList<>(new HashSet<>(recipe2DTOS));
    }


    public Recipe2DTO createRecipe(String title, Set<IngredientDTO> ingredients, Set<InstructionDTO> instructions, Set<CategoryDTO> categories) {
        Recipe2 r2 = new Recipe2(title);
        Set<Category> categorySet = new HashSet<>();
        Set<Instruction> instructionSet = new HashSet<>();
        Set<Ingredient> ingredientSet = new HashSet<>();
        r2 = recipe2Repository.save(r2);
        //TODO: remember to do Entity Manager / detach for related / already input categories,ingredients,instructions
        for (CategoryDTO category : categories) {
            Category c = new Category(category.getName());
            c = categoryRepository.save(c);
            categorySet.add(c);
        }
        for (IngredientDTO ing : ingredients) {
            Ingredient ing2 = new Ingredient(ing.getContent(), ing.getQuantity(), ing.getMeasure());
            ing2 = ingredientRepository.save(ing2);
            ingredientSet.add(ing2);
        }
        for (InstructionDTO ins : instructions) {
            Instruction ins2 = new Instruction(ins.getContent(), ins.getInstructionOrder());
            ins2 = instructionRepository.save(ins2);
            instructionSet.add(ins2);
        }
        r2.setCategories(categorySet);
        r2.setIngredients(ingredientSet);
        r2.setInstructions(instructionSet);
        r2 = recipe2Repository.save(r2);
        Recipe2DTO dto = modelMapper.map(r2, Recipe2DTO.class);
        return dto;
    }

    public Recipe2DTO createRecipe(Recipe2DTO recipe2DTO) {
        Recipe2 r2 = new Recipe2(
                recipe2DTO.getTitle(),
                recipe2DTO.getPhotoUrl(),
                recipe2DTO.getCookTime(),
                recipe2DTO.getPrepTime()
        );
        r2 = recipe2Repository.save(r2);
        //TODO: remember to do Entity Manager / detach for related / already input categories,ingredients,instructions
        for (CategoryDTO categoryDTO : recipe2DTO.getCategories()) {
            Category c = modelMapper.map(categoryDTO, Category.class);
            r2.addCategory(c);
        }
        for (IngredientDTO ingredientDTO : recipe2DTO.getIngredients()) {
            try {
                Ingredient ingredient = modelMapper.map(ingredientDTO, Ingredient.class);
                r2.addIngredient(ingredient);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        for (InstructionDTO instructionDTO : recipe2DTO.getInstructions()) {
            Instruction instruction = modelMapper.map(instructionDTO, Instruction.class);
            r2.addInstruction(instruction);
        }
        r2 = recipe2Repository.save(r2);
        Recipe2DTO returnedRecipeDTO = modelMapper.map(r2, Recipe2DTO.class);

        return returnedRecipeDTO;
    }

    public Recipe2DTO addRecipe(Recipe2DTO newRecipe) {
        Recipe2 r2 = modelMapper.map(newRecipe, Recipe2.class);
        r2 = recipe2Repository.save(r2);
        Recipe2DTO dto = modelMapper.map(r2, Recipe2DTO.class);
        return dto;
    }

    public void addNutrientEntitiesByRecipe(Long recipeId){
        Optional<Recipe2> r2 = recipe2Repository.findById(recipeId);
        if(r2.isPresent()) {
            Recipe2DTO recipe2DTO = modelMapper.map(r2.get(),Recipe2DTO.class);
            for (IngredientDTO ingredientDTO : recipe2DTO.getIngredients()) {
                spoonacularService.addIngredientV2(ingredientDTO, recipe2DTO.getId());
            }
        }
    }

    public Recipe2DTO getRecipeById(Long id) {
        Optional<Recipe2> r = recipe2Repository.findById(id);
        // TODO: Handle the case when id is not found
        if (r.isPresent()) {
            Recipe2 recipe2 = r.get();
            User2DTO user2 = this.getUserData(id);
            Recipe2DTO dto = modelMapper.map(recipe2, Recipe2DTO.class); // TODO: Why is categories not being picked up?
            dto.setUser2(user2);
            return dto;
        }
        return null;
    }

    public Recipe2DTO updateRecipe(Recipe2DTO recipe2DTO) {
        try {
            Recipe2 r2Update = modelMapper.map(recipe2DTO, Recipe2.class);
            Optional<Recipe2> pulledFromDB = recipe2Repository.findById(r2Update.getId());
            Recipe2 originalRecipe = pulledFromDB.get();
            originalRecipe.setInstructions(new HashSet<>());
            for (Instruction instruction : r2Update.getInstructions()) {
                if(instruction.getId()!=null) {
                    Optional<Instruction> insFromDB = instructionRepository.findById(instruction.getId());
                    originalRecipe.addInstruction(insFromDB.get());
                }
                else{
                    Instruction savedInstruction = instructionRepository.save(instruction);
                    originalRecipe.addInstruction(savedInstruction);
                }
            }
            originalRecipe.setIngredients(new HashSet<>());
            for (Ingredient ingredient : r2Update.getIngredients()) {
                if(ingredient.getId()!=null) {
                    Optional<Ingredient> ingFromDB = ingredientRepository.findById(ingredient.getId());
                    originalRecipe.addIngredient(ingFromDB.get());
                }
                else{
                    Ingredient savedIngredient = ingredientRepository.save(ingredient);
                    originalRecipe.addIngredient(savedIngredient);
                }
            }
            originalRecipe.setCategories(new HashSet<>());
            for (Category category : r2Update.getCategories()) {
                if(category.getId()!=null) {
                    Optional<Category> catFromDB = categoryRepository.findById(category.getId());
                    originalRecipe.addCategory(catFromDB.get());
                }
                else{
                    Category savedCategory = categoryRepository.save(category);
                    originalRecipe.addCategory(savedCategory);
                }
            }
            originalRecipe.setNotes(new HashSet<>());
            for (RecipeUpdateNote note : r2Update.getNotes()) {
                if(note.getId()!=null) {
                    Optional<RecipeUpdateNote> noteFromDB = recipeUpdateNoteRepository.findById(note.getId());
                    originalRecipe.addNote(noteFromDB.get());
                }
                else{
                    RecipeUpdateNote savedNote = recipeUpdateNoteRepository.save(note);
                    originalRecipe.addNote(savedNote);
                }
            }
            Recipe2 completedRecipe = recipe2Repository.save(r2Update);
            return modelMapper.map(completedRecipe, Recipe2DTO.class);
        } catch (Exception ex) {
            System.out.println("EXCEPTION HERE");
            ex.printStackTrace();
            return recipe2DTO;
        }
    }

    public Set<Recipe2DTO> findRecipesByUserSearch(String userSearch) {
        Set<Recipe2> recipes = recipe2Repository.searchForRecipes2(userSearch);
        Set<Recipe2DTO> recipeDTOs = new HashSet<>();
        for (Recipe2 recipe: recipes) {
            recipeDTOs.add(modelMapper.map(recipe, Recipe2DTO.class));
        }
        return recipeDTOs;
    }

    public Set<Recipe2DTO> findRecipesByUserId(String userId) {
        Set<Recipe2> recipes = recipe2Repository.findAllByUser(Long.parseLong(userId));
        Set<Recipe2DTO> recipeDTOs = new HashSet<>();
        for (Recipe2 recipe: recipes) {
            recipeDTOs.add(modelMapper.map(recipe, Recipe2DTO.class));
        }
        return recipeDTOs;
    }

    public List<NutrientEntityDTO> getNutrientsByRecipe(Long id) {
        Optional<List<NutrientEntity>> optionalNutrientEntityList = nutrientEntityRepository.findByRecipeId(id);
        if(optionalNutrientEntityList.isPresent())
        {
            List<NutrientEntityDTO> dtoList = new ArrayList<>();
            List<NutrientEntity> neList = optionalNutrientEntityList.get();
            for(NutrientEntity NE:neList){
                dtoList.add(modelMapper.map(NE,NutrientEntityDTO.class));
            }
            return dtoList;
        }
        return null;
    }


}
