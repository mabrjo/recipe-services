package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User2DTO {
    private Set<Recipe2DTO> userRecipes;
    private Set<Recipe2DTO> bookmarks;
    private Long id;
    private String username, displayName, email, provider, photoUrl, providerId;
    private boolean enabled, commentNotifications, likeNotifications, newFollowerNotifications, recipeSavedNotifications;
}
