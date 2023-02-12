package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    // this is used for passing data back to frontend
    private String username, displayName, photoUrl;
    private Long id;
    private Set<Recipe2DTO> userRecipes;
    private Set<Recipe2DTO> bookmarks;
    private boolean enabled, commentNotifications, likeNotifications, newFollowerNotifications, recipeSavedNotifications;

}
