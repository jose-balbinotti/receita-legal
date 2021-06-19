package com.example.receitalegal;

import java.util.List;

public class IngredientsDocument {
    List<Ingredient> ingredients;

    public IngredientsDocument() {
    }

    public IngredientsDocument(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
