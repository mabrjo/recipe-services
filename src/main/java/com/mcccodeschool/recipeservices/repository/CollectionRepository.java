package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {


    Optional<List<Collection>> findByUserId(Long userId);
}
