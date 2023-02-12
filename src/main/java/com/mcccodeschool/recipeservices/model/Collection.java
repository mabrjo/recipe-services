package com.mcccodeschool.recipeservices.model;

import javax.persistence.Entity;
import java.util.Set;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Array;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
public class Collection {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String collectionName;

    @NotNull
    private Long userId;


    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="collection_recipes", joinColumns = @JoinColumn(name="collection_id"),
            inverseJoinColumns = @JoinColumn(name="recipe_id"))
    @Fetch(FetchMode.SELECT)
    private List<Recipe2> recipeList;

    @Lob
    private String imageUrl;

    public Collection() {

    }


}


