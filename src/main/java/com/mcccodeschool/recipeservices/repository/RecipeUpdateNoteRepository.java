package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.RecipeUpdateNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeUpdateNoteRepository extends JpaRepository<RecipeUpdateNote, Long> {
}
