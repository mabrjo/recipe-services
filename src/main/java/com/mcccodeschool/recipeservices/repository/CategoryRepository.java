package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.Category;
import com.mcccodeschool.recipeservices.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
