package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByUsername(String username);
    boolean existsByUsername(String username);

    List<String> findUsernamesByUsernameNotNull();
    List<User> findAllByUsernameNotNull();

}
