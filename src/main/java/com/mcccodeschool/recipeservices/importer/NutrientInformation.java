package com.mcccodeschool.recipeservices.importer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutrientInformation {
    private Long id;
    private String image;
    private Long amount;
    private String unit;
    private String[] possibleUnits;
    private EstimatedCost estimatedCost;
    private String consistency;
    private Nutrition nutrition;
    private String[] categoryPath;
}
