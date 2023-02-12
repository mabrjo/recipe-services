package com.mcccodeschool.recipeservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutrientEntityDTO {
    private Long id;
    private Long recipeId;
    private String name;
    private String image;
    private Long amount;
    private String unit;
    private Long estimatedCostValue;
    private String estimatedCostUnit;
    private String consistency;
    private List<NutrientDTO> nutrients;
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
