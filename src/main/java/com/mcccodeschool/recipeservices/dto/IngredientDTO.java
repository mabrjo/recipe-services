package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private Long id;
    @NonNull
    private String content;
    @NonNull
    private Long quantity;
    @NonNull
    private String measure;

    public IngredientDTO(@NonNull String content, @NonNull Long quantity, @NonNull String measure) {
        this.content = content;
        this.quantity = quantity;
        this.measure = measure;
    }
}

