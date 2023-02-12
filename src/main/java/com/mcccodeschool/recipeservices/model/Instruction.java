package com.mcccodeschool.recipeservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(columnDefinition = "text")
    private String content;
    @NotNull
    private Long instructionOrder;

    public Instruction(String instruction,Long order) {
        this.content=instruction;
        this.instructionOrder=order;
    }
}
