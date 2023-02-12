package com.mcccodeschool.recipeservices.dto;

import com.mcccodeschool.recipeservices.model.User2;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Recipe2DTO {
    private Long id;
    @NonNull
    private String title;
    private Set<CategoryDTO> categories;
    private Set<IngredientDTO> ingredients;
    private Set<InstructionDTO> instructions;
    private String photoUrl;
    private Long cookTime;
    private Long prepTime;
    private Set<RecipeUpdateNoteDTO> notes;
    private User2DTO user2;

    public Recipe2DTO (Long id, String title) {
        this.id = id;
        this.title = title;
    }
}


