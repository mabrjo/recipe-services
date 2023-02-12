package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutrientDTO {
    private Long id;
    private String name;
    private Long amount;
    private String unit;
    private Long percentOfDailyNeeds;
}
