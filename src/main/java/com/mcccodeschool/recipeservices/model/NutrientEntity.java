package com.mcccodeschool.recipeservices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NutrientEntity {
    @Id
    private Long id;
    private Long recipeId;
    @Column(unique = true)
    private String name; //EG: Pineapple
    private String image;
    private Long amount;
    private String unit;
    private Long estimatedCostValue;
    private String estimatedCostUnit;
    private String consistency;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="ingredient_nutrients", joinColumns = @JoinColumn(name="nutrientEntity_id"),
            inverseJoinColumns = @JoinColumn(name="nutrient_id"))
    private List<Nutrient> nutrients; // EG: Vitamin C, Iron, Vitamin A, Potassium
    private Long caloricBreakdownPercentProtein;
    private Long caloricBreakdownPercentFat;
    private Long caloricBreakdownPercentCarbs;
    private Long weightPerServingAmount;
    private String weightPerServingUnit;

    @Override
    public String toString(){
        return "NutrientEntity{"+
                "id="+id+
                "recipeId="+recipeId+
                ",name="+name+
                ",image="+image+
                ",amount="+amount+
                ",unite="+unit+
                ",estimatedCostValue="+estimatedCostValue+
                ",estimatedCostUnit="+estimatedCostUnit+
                ",consistency="+consistency+
                ",nutrients="+nutrients+
                ",caloricBreakdownPercentProtein="+caloricBreakdownPercentProtein+
                ",caloricBreakdownPercentFat="+caloricBreakdownPercentFat+
                ",caloricBreakdownPercentCarbs="+caloricBreakdownPercentCarbs+
                ",weightPerServingAmount="+weightPerServingAmount+
                ",weightPerServingUnit="+weightPerServingUnit+
                "}";
    }
}
