package com.example.receitalegal;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String unitType;
    public String ingredient;

    public Ingredient() {

    }

    public Ingredient(String unitType, String ingredient) {
        this.unitType = unitType;
        this.ingredient = ingredient;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
