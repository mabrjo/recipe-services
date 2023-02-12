package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.dto.NutrientEntityDTO;
import com.mcccodeschool.recipeservices.model.NutrientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutrientEntityRepository extends JpaRepository<NutrientEntity, Long> {

//    Optional<NutrientEntity> findByName( String name);

     Optional<NutrientEntity> findByName( String name);
//     List<NutrientEntity> findByName(String name);

     Optional<List<NutrientEntity>> findByRecipeId(Long id);

}
