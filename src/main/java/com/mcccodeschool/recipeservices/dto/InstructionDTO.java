package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructionDTO {
    private Long id;
    private String content;
    private Long instructionOrder;

    public InstructionDTO(String content, Long order)
    {
        this.content=content;
        this.instructionOrder=order;
    }
}
