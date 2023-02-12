package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
