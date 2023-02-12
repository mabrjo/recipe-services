package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.model.User2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface User2Repository extends JpaRepository<User2,Long> {

    User2 findByEmail(String email);
    boolean existsByEmail(String email);
    User2 findByUsername(String username);
    boolean existsByUsername(String username);

    List<String> findUsernamesByUsernameNotNull();
    List<User2> findAllByUsernameNotNull();

    @Query(value = "SELECT * FROM USER2, USER2_RECIPE2 WHERE user2.id = USER2_RECIPE2.USER2_ID AND USER2_RECIPE2.RECIPE2_ID = :recipeId", nativeQuery = true)
    User2 findByRecipe2Id(@Param("recipeId") Long recipeId);
}
