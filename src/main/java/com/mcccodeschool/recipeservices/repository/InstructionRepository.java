package com.mcccodeschool.recipeservices.repository;

import com.mcccodeschool.recipeservices.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long>{
}
