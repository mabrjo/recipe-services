package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
