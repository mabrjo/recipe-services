package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.Category;
import com.mcccodeschool.recipeservices.model.Recipe2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface Recipe2Repository extends JpaRepository<Recipe2, Long> {

    @Query(
        value = "SELECT * FROM recipe2 " +
                "LEFT JOIN recipe2_ingredient ON recipe2.id = recipe2_ingredient.recipe2_id " +
                "LEFT JOIN ingredient ON recipe2_ingredient.ingredient_id = ingredient.id " +
                "LEFT JOIN recipe2_category ON recipe2.id = recipe2_category.recipe2_id " +
                "LEFT JOIN category ON recipe2_category.category_id = category.id " +
                "WHERE recipe2.title LIKE %?1% OR ingredient.content LIKE %?1% OR category.name LIKE %?1%",
        nativeQuery = true
    )
    Set<Recipe2> searchForRecipes2(String userSearch);


    @Query(value = "SELECT * FROM recipe2, recipe2_category WHERE id = recipe2_category.recipe2_id AND recipe2_category.category_id = :categoryId", nativeQuery = true)
    List<Recipe2> findAllByCategory(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM recipe2, recipe2_ingredient WHERE id = recipe2_ingredient.recipe2_id AND recipe2_ingredient.ingredient_id = :ingredientId", nativeQuery = true)
    List<Recipe2> findAllByIngredient(@Param("ingredientId") Long ingredientId);

    @Query(value = "SELECT * FROM recipe2, user2_recipe2 WHERE id = user2_recipe2.recipe2_id AND user2_recipe2.user2_id = :userId", nativeQuery = true)
    Set<Recipe2> findAllByUser(@Param("userId") Long ingredientId);
}
