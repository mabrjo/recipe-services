package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDTO {
    private Long id;
    private String collectionName;
    private List<Recipe2DTO> recipeList;
    private String imageUrl;
    private Long userId;


}
