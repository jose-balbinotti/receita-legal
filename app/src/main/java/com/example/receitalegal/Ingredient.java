package com.example.receitalegal;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String unitType;
    public String ingredient;
    public String quantity;

    public Ingredient() {

    }

    public Ingredient(String unitType, String ingredient, String quantity) {
        this.unitType = unitType;
        this.ingredient = ingredient;
        this.quantity = quantity;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
