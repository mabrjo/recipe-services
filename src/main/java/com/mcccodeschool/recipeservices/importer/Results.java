package com.mcccodeschool.recipeservices.importer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Results {

    private Long id;
    private String name;
    private String image;

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", name=" + name +
                ", image=" + image +
                "}";
    }
}