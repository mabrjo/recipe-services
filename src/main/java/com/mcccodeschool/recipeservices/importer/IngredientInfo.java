package com.mcccodeschool.recipeservices.importer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientInfo {

    private List<Results> results;
    private Long offset;
    private Long number;
    private long totalResults;

    @Override
    public String toString() {
        return "IngredientInfo{" +
                "Results=" + results +
                ", Offset=" + offset +
                ", Number=" + number +
                ", TotalResults=" + totalResults +
                "}";
    }
}