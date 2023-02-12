package com.mcccodeschool.recipeservices.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Recipe2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String photoUrl;
    private Long cookTime;
    private Long prepTime;


    // TODO: Switch to Lazy if there is time.  Note, for small lookup tables EAGER may be ok.
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="recipe2_category", joinColumns = @JoinColumn(name="recipe2_id"),
           inverseJoinColumns = @JoinColumn(name="category_id"))
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="recipe2_ingredient", joinColumns = @JoinColumn(name="recipe2_id"),
            inverseJoinColumns = @JoinColumn(name="ingredient_id"))
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="recipe2_instruction", joinColumns = @JoinColumn(name="recipe2_id"),
            inverseJoinColumns = @JoinColumn(name="instruction_id"))
    private Set<Instruction> instructions = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="recipe2_notes", joinColumns = @JoinColumn(name="recipe2_id"),
            inverseJoinColumns = @JoinColumn(name="notes_id"))
    private Set<RecipeUpdateNote> notes = new HashSet<>();


    public Recipe2(Long id, String title, String photoUrl, Long cookTime, Long prepTime) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
        this.cookTime = cookTime;
        this.prepTime = prepTime;
    }

    public Recipe2(String title, String photoUrl, Long cookTime, Long prepTime) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.cookTime = cookTime;
        this.prepTime = prepTime;
    }

    public Recipe2() {
        title = "";
    }

    public Recipe2(String t) {
        this.title = t;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addCategory(Category c) {
        this.categories.add(c);
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addInstruction(Instruction ins2) {
        this.instructions.add(ins2);
    }

    public Set<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Set<Instruction> instructions) {
        this.instructions = instructions;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getCookTime() {
        return cookTime;
    }

    public void setCookTime(Long cookTime) {
        this.cookTime = cookTime;
    }

    public Long getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Long prepTime) {
        this.prepTime = prepTime;
    }

    public Set<RecipeUpdateNote> getNotes() {
        return notes;
    }

    public void setNotes(Set<RecipeUpdateNote> notes) {
        this.notes = notes;
    }

    public void addNote(RecipeUpdateNote note){this.notes.add(note);}
}
