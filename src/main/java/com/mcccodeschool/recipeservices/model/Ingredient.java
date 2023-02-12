package com.mcccodeschool.recipeservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Data
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String content;
    @NonNull
    private Long quantity;
    @NonNull
    private String measure;


    @ManyToMany(mappedBy = "ingredients")
    private Set<Recipe2> recipes = new HashSet<>();

    public Ingredient(@NonNull String content, @NonNull Long quantity, @NonNull String measure) {
        this.content = content;
        this.quantity = quantity;
        this.measure = measure;
    }

    public Ingredient(String content, Long quantity, String measure, Set<Recipe2> recipe2s) {
        this.content = content;
        this.quantity = quantity;
        this.measure = measure;
        this.recipes = recipe2s;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Set<Recipe2> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe2> recipes) {
        this.recipes = recipes;
    }
}
