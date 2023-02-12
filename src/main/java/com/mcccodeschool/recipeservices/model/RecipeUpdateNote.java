package com.mcccodeschool.recipeservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RecipeUpdateNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String note;
    private String timestamp;

    public RecipeUpdateNote(String note, String timestamp) {
        this.note = note;
        this.timestamp = timestamp;
    }
}
