package com.mcccodeschool.recipeservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user2",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class User2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String provider;

    private boolean enabled, commentNotifications, likeNotifications, newFollowerNotifications, recipeSavedNotifications;
    private String photoUrl, username, displayName;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="User2_recipe2", joinColumns = @JoinColumn(name="user2_id"),
            inverseJoinColumns = @JoinColumn(name="recipe2_id"))
    private Set<Recipe2> userRecipes = new HashSet<>();

    // TODO: Change to collections
    // TODO: Create collection entity
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="bookmarks", joinColumns = @JoinColumn(name="recipe2_id"),
            inverseJoinColumns = @JoinColumn(name="user2_id"))
    private Set<Recipe2> bookmarks = new HashSet<>();

    public User2(String username)
    {this.username=username;}

    public void addRecipe(Recipe2 recipe2)
    {
        userRecipes.add(recipe2);
    }
    public void addBookmark(Recipe2 recipe2)
    {
        bookmarks.add(recipe2);
    }
}
